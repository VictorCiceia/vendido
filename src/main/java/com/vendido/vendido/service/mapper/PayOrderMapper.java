package com.vendido.vendido.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.PayOrderDTO;
import com.vendido.vendido.entity.PayOrderEntity;

@Component
public class PayOrderMapper implements BaseMapper<PayOrderEntity, PayOrderDTO>{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public PayOrderDTO toDTO(final PayOrderEntity entity) {
		final PayOrderDTO dto = new PayOrderDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDocument(entity.getDocument());
		dto.setAddress(entity.getAddress());
		dto.setTotal(entity.getTotal());
		dto.setStatus(entity.getStatus());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUser(this.userMapper.toDetailDTO(entity.getUser()));
		return dto;
	}

	@Override
	public PayOrderEntity toEntity(final PayOrderDTO dto) {
		final PayOrderEntity entity = new PayOrderEntity();
		entity.setStatus(dto.getStatus());
		entity.setName(dto.getUser().getName() + " " + dto.getUser().getLastname());
		entity.setDocument(dto.getUser().getDocument());
		entity.setAddress(dto.getUser().getAddress());
		return entity;
	}
	
	public PayOrderDTO toSaveDTO(final PayOrderEntity entity) {
		final PayOrderDTO dto = new PayOrderDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setAddress(entity.getAddress());
		dto.setDocument(entity.getDocument());
		dto.setTotal(entity.getTotal());
		dto.setStatus(entity.getStatus());
		dto.setCreatedAt(entity.getCreatedAt());
		return dto;
	}
	
	public void updateEntity(final PayOrderEntity entity) {
		entity.setName(entity.getUser().getName() + " " + entity.getUser().getLastname());
		entity.setAddress(entity.getUser().getDetail().getAddress());
		entity.setDocument(entity.getUser().getDetail().getDocument());
	}
	
}
