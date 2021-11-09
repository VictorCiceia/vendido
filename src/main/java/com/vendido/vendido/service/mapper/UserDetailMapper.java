package com.vendido.vendido.service.mapper;

import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.entity.UserDetailEntity;
import com.vendido.vendido.entity.UserEntity;

@Component
public class UserDetailMapper implements BaseMapper<UserDetailEntity, UserDTO> {

	@Override
	public UserDTO toDTO(final UserDetailEntity entity) {
		final UserDTO dto = new UserDTO();
		dto.setAddress(entity.getAddress());
		dto.setDocument(entity.getDocument());
		dto.setReference(entity.getReference());
		dto.setPhone(entity.getPhone());
		return dto;
	}

	@Override
	public UserDetailEntity toEntity(final UserDTO dto) {
		final UserDetailEntity entity = new UserDetailEntity();
		entity.setAddress(dto.getAddress());
		entity.setDocument(dto.getDocument());
		entity.setPhone(dto.getPhone());
		entity.setReference(dto.getReference());
		final UserEntity user = new UserEntity();
		user.setId(dto.getId());
		entity.setUser(user);
		return entity;
	}
	
	public void toDTO(final UserDTO dto, final UserDetailEntity entity) {
		dto.setAddress(entity.getAddress());
		dto.setDocument(entity.getDocument());
		dto.setReference(entity.getReference());
		dto.setPhone(entity.getPhone());
	}

}
