package com.vendido.vendido.service.mapper;

import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.RoleDTO;
import com.vendido.vendido.entity.RoleEntity;

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

}
