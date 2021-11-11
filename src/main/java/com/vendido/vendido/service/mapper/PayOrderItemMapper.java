package com.vendido.vendido.service.mapper;

import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.PayOrderItemDTO;
import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.PayOrderItemEntity;

@Component
public class PayOrderItemMapper implements BaseMapper<PayOrderItemEntity, PayOrderItemDTO> {

	@Override
	public PayOrderItemDTO toDTO(final PayOrderItemEntity entity) {
		final PayOrderItemDTO dto = new PayOrderItemDTO();
		dto.setId(entity.getId());
		dto.setPayOrderId(entity.getPayOrder().getId());
		dto.setProductId(entity.getProduct().getId());
		dto.setProductQuantity(entity.getProductQuantity());
		dto.setProductName(entity.getProductName());
		dto.setProductPriceUnit(entity.getProductPriceUnit());
		dto.setTotal(entity.getTotal());
		return dto;
	}

	@Override
	public PayOrderItemEntity toEntity(final PayOrderItemDTO dto) {
		final PayOrderItemEntity entity = new PayOrderItemEntity();
		entity.setProductQuantity(dto.getProductQuantity());
		entity.setProductName(dto.getProductName());
		entity.setProductPriceUnit(dto.getProductPriceUnit());
		entity.setTotal(dto.getTotal());
		return entity;
	}
	
	public PayOrderItemEntity toEntity(final ProductDTO productDTO, final int quantity) {
		final PayOrderItemEntity entity = new PayOrderItemEntity();
		entity.setProductQuantity(quantity);
		entity.setProductName(productDTO.getName());
		entity.setProductPriceUnit(productDTO.getPrice());
		return entity;
	}

}
