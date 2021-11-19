package com.vendido.vendido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.OrderItemDTO;
import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.OrderEntity;
import com.vendido.vendido.entity.OrderItemEntity;
import com.vendido.vendido.entity.ProductEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.OrderItemRepository;
import com.vendido.vendido.resource.OrderItemResource;
import com.vendido.vendido.service.mapper.OrderItemMapper;
import com.vendido.vendido.service.mapper.ProductMapper;

@Service
public class OrderItemService implements BaseService<OrderItemDTO, OrderItemResource> {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductMapper productMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderItemResource findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderItemDTO findById(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<OrderItemDTO> findByOrderId(final long id) throws Exception {
		List<OrderItemEntity>entities = this.orderItemRepository.findByOrderId(id);
		if(!entities.isEmpty()) {
			return entities.stream().map(this.orderItemMapper::toDTO).collect(Collectors.toList());
		}else {
			return new ArrayList<>();
		}	
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public OrderItemDTO save(final OrderItemDTO dto) throws Exception {
		//Busca el producto
		ProductDTO productDTO = this.productService.findById(dto.getProduct().getId());
		ProductEntity productEntity = this.productMapper.toEntity(productDTO);
		productEntity.setId(productDTO.getId());
		
		//Creando la entidad del item
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(dto.getOrderId());
		
		OrderItemEntity entity = this.orderItemMapper.toEntity(dto);
		entity.setOrder(orderEntity);
		entity.setProduct(productEntity);
		
		OrderItemDTO saveDTO = this.orderItemMapper.toDTO(this.orderItemRepository.save(entity));
		saveDTO.setProduct(productDTO);
		return saveDTO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public OrderItemDTO update(final long id, final OrderItemDTO dto) throws Exception {
		
		//Se busca el item
		OrderItemEntity entity = this.orderItemRepository.findById(id)//
				.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		
		//Busca el producto
		ProductDTO productDTO = this.productService.findById(dto.getProduct().getId());
		ProductEntity productEntity = this.productMapper.toEntity(productDTO);
		productEntity.setId(productDTO.getId());
		
		entity.setProductQuantity(dto.getProductQuantity());
		entity.setProduct(productEntity);
		
		OrderItemDTO saveDTO = this.orderItemMapper.toDTO(this.orderItemRepository.save(entity));
		saveDTO.setProduct(productDTO);
		return saveDTO;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		//Se busca el item
		OrderItemEntity entity = this.orderItemRepository.findById(id)//
				.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		this.orderItemRepository.delete(entity);
	}

}
