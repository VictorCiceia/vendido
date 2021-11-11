package com.vendido.vendido.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.OrderDTO;
import com.vendido.vendido.entity.OrderEntity;

@Component
public class OrderMapper implements BaseMapper<OrderEntity, OrderDTO>{
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public OrderDTO toDTO(final OrderEntity entity) {
		final OrderDTO dto = new OrderDTO();
		dto.setId(entity.getId());
		dto.setTotal(entity.getTotal());
		dto.setTax_total(entity.getTaxTotal());
		dto.setProductQuantity(entity.getProductQuantity());
		dto.setStatus(entity.getStatus());
		dto.setCreatedAt(entity.getCreated_at());
		dto.setUser(this.userMapper.toDetailDTO(entity.getUser()));
		return dto;
	}
	
	public OrderDTO toSaveDTO(final OrderEntity entity) {
		final OrderDTO dto = new OrderDTO();
		dto.setId(entity.getId());
		dto.setTotal(entity.getTotal());
		dto.setTax_total(entity.getTaxTotal());
		dto.setProductQuantity(entity.getProductQuantity());
		dto.setStatus(entity.getStatus());
		dto.setCreatedAt(entity.getCreated_at());
		return dto;
	}

	@Override
	public OrderEntity toEntity(final OrderDTO dto) {
		OrderEntity entity = new OrderEntity();
		return entity;
	}

}
