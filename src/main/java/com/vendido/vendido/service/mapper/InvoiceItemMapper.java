package com.vendido.vendido.service.mapper;

import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.InvoiceItemDTO;
import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.InvoiceItemEntity;

@Component
public class InvoiceItemMapper implements BaseMapper<InvoiceItemEntity, InvoiceItemDTO> {

	@Override
	public InvoiceItemDTO toDTO(final InvoiceItemEntity entity) {
		final InvoiceItemDTO dto = new InvoiceItemDTO();
		dto.setId(entity.getId());
		dto.setInvoiceId(entity.getInvoice().getId());
		dto.setProductId(entity.getProduct().getId());
		dto.setProductQuantity(entity.getProductQuantity());
		dto.setProductName(entity.getProductName());
		dto.setProductTaxPercentage(entity.getProductTaxPercentage());
		dto.setProductCostUnit(entity.getProductCostUnit());
		dto.setProductPriceUnit(entity.getProductPriceUnit());
		dto.setTotal(entity.getTotal());
		dto.setTaxTotal(entity.getTaxTotal());
		return dto;
	}

	@Override
	public InvoiceItemEntity toEntity(final InvoiceItemDTO dto) {
		final InvoiceItemEntity entity = new InvoiceItemEntity();
		entity.setProductQuantity(dto.getProductQuantity());
		entity.setProductName(dto.getProductName());
		entity.setProductCostUnit(dto.getProductCostUnit());
		entity.setProductPriceUnit(dto.getProductPriceUnit());
		entity.setProductTaxPercentage(dto.getProductTaxPercentage());
		entity.setTaxTotal(dto.getTaxTotal());
		entity.setTotal(dto.getTotal());
		return entity;
	}
	
	public InvoiceItemEntity toEntity(final ProductDTO productDTO, final int quantity) {
		final InvoiceItemEntity entity = new InvoiceItemEntity();
		entity.setProductQuantity(quantity);
		entity.setProductName(productDTO.getName());
		entity.setProductCostUnit(productDTO.getCost());
		entity.setProductPriceUnit(productDTO.getPrice());
		entity.setProductTaxPercentage(productDTO.getTaxPercentage());
		return entity;
	}

}
