package com.vendido.vendido.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vendido.vendido.dto.InvoiceDTO;
import com.vendido.vendido.entity.InvoiceEntity;

@Component
public class InvoiceMapper implements BaseMapper<InvoiceEntity, InvoiceDTO>{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public InvoiceDTO toDTO(final InvoiceEntity entity) {
		final InvoiceDTO dto = new InvoiceDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDocument(entity.getDocument());
		dto.setAddress(entity.getAddress());
		dto.setTotal(entity.getTotal());
		dto.setTax_total(entity.getTaxTotal());
		dto.setPayMethodd(entity.getPayMethod());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUser(this.userMapper.toDetailDTO(entity.getUser()));
		return dto;
	}

	@Override
	public InvoiceEntity toEntity(final InvoiceDTO dto) {
		final InvoiceEntity entity = new InvoiceEntity();
		return entity;
	}

}
