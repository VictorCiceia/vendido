package com.vendido.vendido.service.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.RoleDTO;
import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.entity.RoleEntity;
import com.vendido.vendido.entity.UserEntity;

@Component
public class RoleMapper implements BaseMapper<RoleEntity, RoleDTO> {

	@Override
	public RoleDTO toDTO(final RoleEntity entity) {
		RoleDTO dto = new RoleDTO();
		dto.setId(entity.getId());
		dto.setAuthority(entity.getAuthority());
		return dto;
	}

	@Override
	public RoleEntity toEntity(final RoleDTO dto) {
		RoleEntity entity = new RoleEntity();
		entity.setId(dto.getId());
		entity.setAuthority(dto.getAuthority());
		return entity;
	}

	public void toDTO(final UserDTO dto, final List<RoleEntity> entities) {
		if (null == dto.getRoles())
			dto.setRoles(new ArrayList<>());

		for (RoleEntity entity : entities) {
			dto.getRoles().add(toDTO(entity));
		}
	}
	
	public void toEntity(final UserEntity entity, final List<RoleDTO> dtos) {
		if (null == entity.getRoles())
			entity.setRoles(new ArrayList<>());

		for (RoleDTO dto : dtos) {
			entity.getRoles().add(toEntity(dto));
		}
	}

}
