package com.vendido.vendido.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.OrderItemDTO;
import com.vendido.vendido.entity.OrderItemEntity;

@Component
public class OrderItemMapper implements BaseMapper<OrderItemEntity, OrderItemDTO>{
		
	@Autowired
	private ProductMapper productMapper;

	@Override
	public OrderItemDTO toDTO(final OrderItemEntity entity) {
		OrderItemDTO dto = new OrderItemDTO();
		dto.setId(entity.getId());
		dto.setOrderId(entity.getOrder().getId());
		dto.setProductQuantity(entity.getProductQuantity());
		dto.setProduct(this.productMapper.toDTO(entity.getProduct()));
		return dto;
	}

	@Override
	public OrderItemEntity toEntity(OrderItemDTO dto) {
		final OrderItemEntity entity = new OrderItemEntity();
		entity.setProductQuantity(dto.getProductQuantity());
		return entity;
	}

}
