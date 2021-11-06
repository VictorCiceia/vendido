package com.vendido.vendido.service.mapper;

public interface BaseMapper<Entity, DTO> {

	public DTO toDTO(final Entity entity);

	public Entity toEntity(final DTO dto);

}