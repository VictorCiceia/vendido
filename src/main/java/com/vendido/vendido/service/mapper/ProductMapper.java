package com.vendido.vendido.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.ProductEntity;

@Component
public class ProductMapper implements BaseMapper<ProductEntity, ProductDTO>{
	
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public ProductDTO toDTO(final ProductEntity entity) {
		final ProductDTO dto = new ProductDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setCost(entity.getCost());
		dto.setPrice(entity.getPrice());
		dto.setAmount(entity.getAmount());
		dto.setTaxPercentage(entity.getTax_percentage());
		dto.setCreatedAt(entity.getCreated_at());
		dto.setCategory(this.categoryMapper.toDTO(entity.getCategory()));
		return dto;
	}

	@Override
	public ProductEntity toEntity(ProductDTO dto) {
		final ProductEntity entity = new ProductEntity();
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setCost(dto.getCost());
		entity.setPrice(dto.getPrice());
		entity.setAmount(dto.getAmount());
		entity.setTax_percentage(dto.getTaxPercentage());
		entity.setCreated_at(dto.getCreatedAt());
		entity.setCategory(this.categoryMapper.toEntity(dto.getCategory()));
		return entity;
	}

}
