package com.vendido.vendido.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.PayOrderDTO;
import com.vendido.vendido.dto.PayOrderItemDTO;
import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.entity.PayOrderEntity;
import com.vendido.vendido.entity.UserEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.PayOrderRepository;
import com.vendido.vendido.resource.PayOrderResource;
import com.vendido.vendido.service.mapper.PayOrderMapper;
import com.vendido.vendido.service.mapper.UserMapper;

@Service
public class PayOrderService implements BaseService<PayOrderDTO, PayOrderResource>{
	
	@Autowired
	private PayOrderRepository payOrderRepository;
	
	@Autowired
	private PayOrderMapper payOrderMapper;
	
	@Autowired
	private PayOrderItemService payOrderItemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PayOrderResource findAll(final Pageable pageable) {
		final Page<PayOrderDTO> page = this.payOrderRepository.findAllByDeleted(false, pageable)//
				.map(this.payOrderMapper::toDTO);
		final PayOrderResource res = new PayOrderResource();
		res.setList(page.getContent());
		return res;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PayOrderDTO findById(final long id) throws Exception {
		final PayOrderDTO dto = this.payOrderRepository.findByIdAndDeleted(id, false)//
				.map(this.payOrderMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		dto.setItems(this.payOrderItemService.findByPayOrderId(id));
		return dto;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PayOrderDTO save(final PayOrderDTO dto) throws Exception {
		
		//Busca si existe el usuario
		final UserDTO userDto = this.userService.findById(dto.getUser().getId());
		dto.setUser(userDto);
		final UserEntity userEntity = this.userMapper.toEntity(userDto);
		userEntity.setId(userDto.getId());
		
		//Creando la cabecera para obtener el id
		
		PayOrderEntity entity = this.payOrderMapper.toEntity(dto);
		entity.setUser(userEntity);
		entity.setCreatedAt(new Date());
		entity.setDeleted(false);
		entity.setTotal(0);	
		PayOrderEntity saveEntity = this.payOrderRepository.save(entity);	
		
		//Guardando con los detalles
		List<PayOrderItemDTO> listItems = getItemEntity(saveEntity, dto.getItems());
		
		//Guardando los totales
		saveEntity = this.payOrderRepository.save(saveEntity);	
		
		PayOrderDTO orderDTO = this.payOrderMapper.toSaveDTO(saveEntity);
		orderDTO.setUser(userDto);
		orderDTO.setItems(listItems);
		
		return orderDTO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PayOrderDTO update(final long id, PayOrderDTO dto) throws Exception {
		//Buscando orden
		PayOrderEntity entity = this.payOrderRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		
		//Buscando el nuevo usuario
		final UserDTO userDto = this.userService.findById(dto.getUser().getId());
		final UserEntity userEntity = this.userMapper.toEntity(userDto);
		userEntity.setId(userDto.getId());
		
		this.payOrderMapper.updateEntity(entity);
		entity.setStatus(dto.getStatus());
		
		//Guardando con los detalles
		List<PayOrderItemDTO> oldItems = this.payOrderItemService.findByPayOrderId(id);
		List<PayOrderItemDTO> listItems = updateItemEntity(entity, dto.getItems(), oldItems);
		
		//Guardando los totales
		PayOrderEntity saveEntity = this.payOrderRepository.save(entity);	
		
		PayOrderDTO orderDTO = this.payOrderMapper.toSaveDTO(saveEntity);
		orderDTO.setUser(userDto);
		orderDTO.setItems(listItems);
		
		return orderDTO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		PayOrderEntity entity = this.payOrderRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		entity.setDeleted(true);
		this.payOrderRepository.save(entity);		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private List<PayOrderItemDTO> getItemEntity(final PayOrderEntity order, final List<PayOrderItemDTO> items) throws Exception{
		final List<PayOrderItemDTO>  listItems = new ArrayList<>();

		int total = 0;
		for(PayOrderItemDTO item : items) {
			
			item.setPayOrderId(order.getId());
			PayOrderItemDTO savedItem = this.payOrderItemService.save(item);
			
			//Agregando el item a la lista
			listItems.add(savedItem);
			total += savedItem.getProductPriceUnit() * item.getProductQuantity();
		}
		
		//Cambiando los valores
		order.setTotal(total);
		return listItems;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private List<PayOrderItemDTO> updateItemEntity(final PayOrderEntity order, final List<PayOrderItemDTO> newItems, final List<PayOrderItemDTO> oldItems) throws Exception{
		final List<PayOrderItemDTO>  listItems = new ArrayList<>();

		int total = 0;
		
		for(PayOrderItemDTO item : newItems) {
			
			//Se busca si existe el item en la orden
			PayOrderItemDTO savedItem = null;
			PayOrderItemDTO exists = findProductList(oldItems, item.getProductId());
			
			//Entra si la orden ya tiene ese producto
			if(exists!=null) {
				//Si las cantidades son distintas entonces se actualiza la cantidad
				if(exists.getProductQuantity() != item.getProductQuantity()) {
					exists.setProductQuantity(item.getProductQuantity());
					savedItem = this.payOrderItemService.update(exists.getId(), exists);
				}
				//Si la cantidad es igual entonces no se hace nada
				else {
					savedItem = exists;
				}
				
			}
			//Si la orden no tiene el producto se registra en la orden
			else {
				item.setPayOrderId(order.getId());
				savedItem = this.payOrderItemService.save(item);
			}
			
			//Agregando el item a la lista
			listItems.add(savedItem);
			//Se suma los montos de precio * cantidad
			total += savedItem.getProductId() * item.getProductQuantity();
		}
		
		//Elimina los items que no aparecen en el listado nuevo
		deleteItems(newItems, oldItems);
		
		//Cambiando los valores
		order.setTotal(total);
		
		return listItems;
	}
	
	private void deleteItems(final List<PayOrderItemDTO> newItems, final List<PayOrderItemDTO> oldItems) throws Exception {
		for(PayOrderItemDTO oldItem : oldItems) {
			PayOrderItemDTO exists = findProductOldList(newItems, oldItem);
			if(null != exists) {
				this.payOrderItemService.delete(exists.getId());
			}
		}
	}
	
	private PayOrderItemDTO findProductOldList(final List<PayOrderItemDTO> newItems, final PayOrderItemDTO oldItem) {
		for(PayOrderItemDTO newItem : newItems) {
			if(oldItem.getProductId() == newItem.getProductId()) {
				return null;
			}
		}
		return oldItem;	
	}
	
	private PayOrderItemDTO findProductList (final List<PayOrderItemDTO> oldItems, final long productId) {
		for(PayOrderItemDTO item : oldItems) {
			if(item.getProductId() == productId) {
				return item;
			}
		}
		return null;
	}

}
