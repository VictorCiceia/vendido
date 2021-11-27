package com.vendido.vendido.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.InvoiceDTO;
import com.vendido.vendido.dto.InvoiceItemDTO;
import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.entity.InvoiceEntity;
import com.vendido.vendido.entity.UserEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.InvoiceRepository;
import com.vendido.vendido.resource.InvoiceResouce;
import com.vendido.vendido.service.mapper.InvoiceMapper;
import com.vendido.vendido.service.mapper.UserMapper;

@Service
public class InvoiceService implements BaseService<InvoiceDTO, InvoiceResouce> {
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceMapper invoiceMapper;
	
	@Autowired
	private InvoiceItemService invoiceItemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public InvoiceResouce findAll(final Pageable pageable) {
		final Page<InvoiceDTO> page =  this.invoiceRepository.findAllByDeleted(false, pageable)//
				.map(this.invoiceMapper::toDTO);
		final InvoiceResouce res = new InvoiceResouce();
		res.setList(page.getContent());
		for(InvoiceDTO i : res.getList()) {
			cacheManager.getCache("invoices").put(i.getId(), i);
		}
		return res;
	}

	@Override
	@Cacheable(cacheNames = "invoices", key = "#id")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public InvoiceDTO findById(final long id) throws Exception {
		final InvoiceDTO dto = this.invoiceRepository.findByIdAndDeleted(id, false)//
				.map(this.invoiceMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		dto.setItems(this.invoiceItemService.findByInvoiceId(id));
		return dto;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InvoiceDTO save(final InvoiceDTO dto) throws Exception {
		//Obtiene al usuario
		final UserDTO userDto = this.userService.findById(dto.getUser().getId());
		dto.setUser(userDto);
		final UserEntity userEntity = this.userMapper.toEntity(userDto);
		userEntity.setId(userDto.getId());
		
		//Creando la cabecera para obtener el id
		InvoiceEntity entity = this.invoiceMapper.toEntity(dto);
		entity.setUser(userEntity);
		entity.setCreatedAt(new Date());
		entity.setDeleted(false);
		entity.setTaxTotal(0);
		entity.setTotal(0);
		InvoiceEntity saveEntity = this.invoiceRepository.save(entity);	
		
		//Guardando con los detalles
		List<InvoiceItemDTO> listItems = getItemEntity(saveEntity, dto.getItems());
		
		//Guardando los totales
		saveEntity = this.invoiceRepository.save(entity);	
		
		InvoiceDTO orderDTO = this.invoiceMapper.toSaveDTO(saveEntity);
		orderDTO.setUser(userDto);
		orderDTO.setItems(listItems);
		
		return orderDTO;
	}

	@Override
	@CachePut(cacheNames = "invoices", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InvoiceDTO update(final long id, final InvoiceDTO dto) throws Exception {
		//Buscando orden
		final InvoiceEntity entity = this.invoiceRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		
		//Buscando el nuevo usuario
		final UserDTO userDto = this.userService.findById(dto.getUser().getId());
		final UserEntity userEntity = this.userMapper.toEntity(userDto);
		userEntity.setId(userDto.getId());
		
		this.invoiceMapper.updateEntity(entity);
		entity.setPayMethod(dto.getPayMethodd());
		
		//Guardando con los detalles
		List<InvoiceItemDTO> oldItems = this.invoiceItemService.findByInvoiceId(id);
		List<InvoiceItemDTO> listItems = updateItemEntity(entity, dto.getItems(), oldItems);
		
		//Guardando los totales
		InvoiceEntity saveEntity = this.invoiceRepository.save(entity);	
		
		InvoiceDTO invoiceDTO = this.invoiceMapper.toSaveDTO(saveEntity);
		invoiceDTO.setUser(userDto);
		invoiceDTO.setItems(listItems);
		
		return invoiceDTO;
	}

	@Override
	@CacheEvict(cacheNames = "invoices", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		final InvoiceEntity entity = this.invoiceRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		entity.setDeleted(true);
		this.invoiceRepository.save(entity);	
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private List<InvoiceItemDTO> getItemEntity(final InvoiceEntity invoice, final List<InvoiceItemDTO> items) throws Exception{
		final List<InvoiceItemDTO>  listItems = new ArrayList<>();

		int total = 0;
		int taxTotal = 0;
		for(InvoiceItemDTO item : items) {
			
			item.setInvoiceId(invoice.getId());
			InvoiceItemDTO savedItem = this.invoiceItemService.save(item);
			
			//Agregando el item a la lista
			listItems.add(savedItem);
			total += savedItem.getProductPriceUnit() * item.getProductQuantity();
			taxTotal += (int)((double)(savedItem.getProductPriceUnit() * item.getProductQuantity()) * ((double)((double)savedItem.getProductTaxPercentage())/100));
		}
		
		//Cambiando los valores
		invoice.setTaxTotal(taxTotal);
		invoice.setTotal(total);
		
		return listItems;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private List<InvoiceItemDTO> updateItemEntity(final InvoiceEntity invoice, final List<InvoiceItemDTO> newItems, final List<InvoiceItemDTO> oldItems) throws Exception{
		final List<InvoiceItemDTO>  listItems = new ArrayList<>();

		int total = 0;
		int taxTotal = 0;
		
		for(InvoiceItemDTO item : newItems) {
			
			//Se busca si existe el item en la orden
			InvoiceItemDTO savedItem = null;
			InvoiceItemDTO exists = findProductList(oldItems, item.getProductId());
			
			//Entra si la orden ya tiene ese producto
			if(exists!=null) {
				//Si las cantidades son distintas entonces se actualiza la cantidad
				if(exists.getProductQuantity() != item.getProductQuantity()) {
					exists.setProductQuantity(item.getProductQuantity());
					savedItem = this.invoiceItemService.update(exists.getId(), exists);
				}
				//Si la cantidad es igual entonces no se hace nada
				else {
					savedItem = exists;
				}
				
			}
			//Si la orden no tiene el producto se registra en la orden
			else {
				item.setInvoiceId(invoice.getId());
				savedItem = this.invoiceItemService.save(item);
			}
			
			//Agregando el item a la lista
			listItems.add(savedItem);
			//Se suma los montos de precio * cantidad
			total += savedItem.getProductPriceUnit() * item.getProductQuantity();
			taxTotal += (int)((double)(savedItem.getProductPriceUnit()  * item.getProductQuantity()) * ((double)((double)savedItem.getProductTaxPercentage())/100));
		}
		
		//Elimina los items que no aparecen en el listado nuevo
		deleteItems(newItems, oldItems);
		
		//Cambiando los valores
		invoice.setTaxTotal(taxTotal);
		invoice.setTotal(total);
		
		return listItems;
	}
	
	private void deleteItems(final List<InvoiceItemDTO> newItems, final List<InvoiceItemDTO> oldItems) throws Exception {
		for(InvoiceItemDTO oldItem : oldItems) {
			InvoiceItemDTO exists = findProductOldList(newItems, oldItem);
			if(null != exists) {
				this.invoiceItemService.delete(exists.getId());
			}
		}
	}
	
	private InvoiceItemDTO findProductOldList(final List<InvoiceItemDTO> newItems, final InvoiceItemDTO oldItem) {
		for(InvoiceItemDTO newItem : newItems) {
			if(oldItem.getProductId() == newItem.getProductId()) {
				return null;
			}
		}
		return oldItem;	
	}
	
	private InvoiceItemDTO findProductList (final List<InvoiceItemDTO> oldItems, final long productId) {
		for(InvoiceItemDTO item : oldItems) {
			if(item.getProductId() == productId) {
				return item;
			}
		}
		return null;
	}

}
