package com.vendido.vendido.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.entity.UserEntity;

@Component
public class UserMapper implements BaseMapper<UserEntity, UserDTO> {
	
	@Autowired
	private UserDetailMapper userDetailMapper;
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public UserDTO toDTO(final UserEntity entity) {
		final UserDTO dto = new UserDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setLastname(entity.getLastname());
		dto.setEmail(entity.getEmail());		
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setOldPassword(entity.getPassword());
		return dto;
	}

	@Override
	public UserEntity toEntity(final UserDTO dto) {
		final UserEntity entity = new UserEntity();
		entity.setName(dto.getName());
		entity.setLastname(dto.getLastname());
		entity.setEmail(dto.getEmail());
		entity.setRoles(null);
		this.roleMapper.toEntity(entity, dto.getRoles());
		return entity;
	}
	
	public UserDTO toDetailDTO(final UserEntity entity) {
		final UserDTO dto = toDTO(entity);	
		userDetailMapper.toDTO(dto, entity.getDetail());
		roleMapper.toDTO(dto, entity.getRoles());
		return dto;
	}
	
	public UserDTO toRDTO(final UserEntity entity) {
		final UserDTO dto = toDTO(entity);	
		roleMapper.toDTO(dto, entity.getRoles());
		return dto;
	}
	
	public void toUpdateEntity(final UserEntity entity, final UserDTO dto) {
		entity.setName(dto.getName());
		entity.setLastname(dto.getLastname());
		entity.getDetail().setAddress(dto.getAddress());
		entity.getDetail().setDocument(dto.getDocument());
		entity.getDetail().setReference(dto.getReference());
		entity.getDetail().setPhone(dto.getPhone());
	}

}
