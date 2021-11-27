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

import com.vendido.vendido.dto.OrderDTO;
import com.vendido.vendido.dto.OrderItemDTO;
import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.entity.OrderEntity;
import com.vendido.vendido.entity.UserEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.OrderRepository;
import com.vendido.vendido.resource.OrderResource;
import com.vendido.vendido.service.mapper.OrderMapper;
import com.vendido.vendido.service.mapper.UserMapper;

@Service
public class OrderService implements BaseService<OrderDTO, OrderResource> {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderResource findAll(final Pageable pageable) {
		final Page<OrderDTO> page = this.orderRepository.findAllByDeleted(false, pageable)//
				.map(this.orderMapper::toDTO);
		final OrderResource res = new OrderResource();
		res.setList(page.getContent());
		for(OrderDTO o : res.getList()) {
			cacheManager.getCache("ordes").put(o.getId(), o);
		}
		return res;
	}

	@Override
	@Cacheable(cacheNames = "ordes", key = "#id")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderDTO findById(final long id) throws Exception {
		
		final OrderDTO dto = this.orderRepository.findByIdAndDeleted(id, false)//
				.map(this.orderMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Orden", "id", id));
		
		dto.setItems(this.orderItemService.findByOrderId(dto.getId()));
		return dto;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public OrderDTO save(final OrderDTO dto) throws Exception {
		final UserDTO userDto = this.userService.findById(dto.getUser().getId());
		final UserEntity userEntity = this.userMapper.toEntity(userDto);
		userEntity.setId(userDto.getId());
		
		//Creando la cabecera para obtener el id
		OrderEntity entity = this.orderMapper.toEntity(dto);
		entity.setUser(userEntity);
		entity.setCreated_at(new Date());
		entity.setDeleted(false);
		entity.setTaxTotal(0);
		entity.setTotal(0);
		entity.setProductQuantity(0);
		entity.setStatus("Pendiente");	
		OrderEntity saveEntity = this.orderRepository.save(entity);	
		
		//Guardando con los detalles
		List<OrderItemDTO> listItems = getItemEntity(saveEntity, dto.getItems());
		
		//Guardando los totales
		saveEntity = this.orderRepository.save(entity);	
		
		OrderDTO orderDTO = this.orderMapper.toSaveDTO(saveEntity);
		orderDTO.setUser(userDto);
		orderDTO.setItems(listItems);
		
		return orderDTO;
	}

	@Override
	@CachePut(cacheNames = "ordes", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public OrderDTO update(long id, OrderDTO dto) throws Exception {
		
		//Buscando orden
		final OrderEntity entity = this.orderRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Orden", "id", id));
		
		//Buscando el nuevo usuario
		final UserDTO userDto = this.userService.findById(dto.getUser().getId());
		final UserEntity userEntity = this.userMapper.toEntity(userDto);
		userEntity.setId(userDto.getId());
		
		//Actualizando
		entity.setStatus(dto.getStatus());
		entity.setUser(userEntity);
		
		//Guardando con los detalles
		List<OrderItemDTO> oldItems = this.orderItemService.findByOrderId(id);
		List<OrderItemDTO> listItems = updateItemEntity(entity, dto.getItems(), oldItems);
		
		//Guardando los totales
		OrderEntity saveEntity = this.orderRepository.save(entity);	
		
		OrderDTO orderDTO = this.orderMapper.toSaveDTO(saveEntity);
		orderDTO.setUser(userDto);
		orderDTO.setItems(listItems);
		
		return orderDTO;
	}

	@Override
	@CacheEvict(cacheNames = "ordes", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(long id) throws Exception {
		OrderEntity entity = this.orderRepository.findByIdAndDeleted(id, false)//
			.orElseThrow(() -> new ResourceNotFoundException("Orden", "id", id));
		
		entity.setDeleted(true);
		this.orderRepository.save(entity);	
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private List<OrderItemDTO> getItemEntity(final OrderEntity order, final List<OrderItemDTO> items) throws Exception{
		final List<OrderItemDTO>  listItems = new ArrayList<>();

		int total = 0;
		int taxTotal = 0;
		int productQuantity = 0;
		for(OrderItemDTO item : items) {
			
			item.setOrderId(order.getId());
			OrderItemDTO savedItem = this.orderItemService.save(item);
			
			//Agregando el item a la lista
			listItems.add(savedItem);
			total += savedItem.getProduct().getPrice() * item.getProductQuantity();
			taxTotal += (int)((double)(savedItem.getProduct().getPrice() * item.getProductQuantity()) * ((double)((double)savedItem.getProduct().getTaxPercentage())/100));
			productQuantity++;
		}
		
		//Cambiando los valores
		order.setTaxTotal(taxTotal);
		order.setTotal(total);
		order.setProductQuantity(productQuantity);
		
		return listItems;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private List<OrderItemDTO> updateItemEntity(final OrderEntity order, final List<OrderItemDTO> newItems, final List<OrderItemDTO> oldItems) throws Exception{
		final List<OrderItemDTO>  listItems = new ArrayList<>();

		int total = 0;
		int taxTotal = 0;
		int productQuantity = 0;
		
		for(OrderItemDTO item : newItems) {
			
			//Se busca si existe el item en la orden
			OrderItemDTO savedItem = null;
			OrderItemDTO exists = findProductList(oldItems, item.getProduct().getId());
			
			//Entra si la orden ya tiene ese producto
			if(exists!=null) {
				//Si las cantidades son distintas entonces se actualiza la cantidad
				if(exists.getProductQuantity() != item.getProductQuantity()) {
					exists.setProductQuantity(item.getProductQuantity());
					savedItem = this.orderItemService.update(exists.getId(), exists);
				}
				//Si la cantidad es igual entonces no se hace nada
				else {
					savedItem = exists;
				}
				
			}
			//Si la orden no tiene el producto se registra en la orden
			else {
				item.setOrderId(order.getId());
				savedItem = this.orderItemService.save(item);
			}
			
			//Agregando el item a la lista
			listItems.add(savedItem);
			//Se suma los montos de precio * cantidad
			total += savedItem.getProduct().getPrice() * item.getProductQuantity();
			taxTotal += (int)((double)(savedItem.getProduct().getPrice() * item.getProductQuantity()) * ((double)((double)savedItem.getProduct().getTaxPercentage())/100));
			productQuantity++;
		}
		
		//Elimina los items que no aparecen en el listado nuevo
		deleteItems(newItems, oldItems);
		
		//Cambiando los valores
		order.setTaxTotal(taxTotal);
		order.setTotal(total);
		order.setProductQuantity(productQuantity);
		
		return listItems;
	}
	
	private void deleteItems(final List<OrderItemDTO> newItems, final List<OrderItemDTO> oldItems) throws Exception {
		for(OrderItemDTO oldItem : oldItems) {
			OrderItemDTO exists = findProductOldList(newItems, oldItem);
			if(null != exists) {
				this.orderItemService.delete(exists.getId());
			}
		}
	}
	
	private OrderItemDTO findProductOldList(final List<OrderItemDTO> newItems, final OrderItemDTO oldItem) {
		for(OrderItemDTO newItem : newItems) {
			if(oldItem.getProduct().getId() == newItem.getProduct().getId()) {
				return null;
			}
		}
		return oldItem;	
	}
	
	private OrderItemDTO findProductList (final List<OrderItemDTO> oldItems, final long productId) {
		for(OrderItemDTO item : oldItems) {
			if(item.getProduct().getId() == productId) {
				return item;
			}
		}
		return null;
	}
}
