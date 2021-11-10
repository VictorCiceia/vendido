package com.vendido.vendido.service.mapper;

import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.CategoryDTO;
import com.vendido.vendido.entity.CategoryEntity;

@Component
public class CategoryMapper implements BaseMapper<CategoryEntity, CategoryDTO> {

	@Override
	public CategoryDTO toDTO(CategoryEntity entity) {
		final CategoryDTO dto = new CategoryDTO();
		dto.setId(entity.getId());
		dto.setCategory(entity.getCategory());
		return dto;
	}

	@Override
	public CategoryEntity toEntity(CategoryDTO dto) {
		final CategoryEntity entity = new CategoryEntity();
		entity.setId(dto.getId());
		entity.setCategory(dto.getCategory());
		return entity;
	}

}
