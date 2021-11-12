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
		entity.setPayMethod(dto.getPayMethodd());
		entity.setName(dto.getUser().getName() + " " + dto.getUser().getLastname());
		entity.setDocument(dto.getUser().getDocument());
		entity.setAddress(dto.getUser().getAddress());
		return entity;
	}
	
	public InvoiceDTO toSaveDTO(final InvoiceEntity entity) {
		final InvoiceDTO dto = new InvoiceDTO();
		dto.setId(entity.getId());
		dto.setTotal(entity.getTotal());
		dto.setTax_total(entity.getTaxTotal());
		dto.setName(entity.getName());
		dto.setAddress(entity.getAddress());
		dto.setDocument(entity.getDocument());
		dto.setPayMethodd(entity.getPayMethod());
		dto.setCreatedAt(entity.getCreatedAt());
		return dto;
	}
	
	public void updateEntity(final InvoiceEntity entity) {
		entity.setName(entity.getUser().getName() + " " + entity.getUser().getLastname());
		entity.setAddress(entity.getUser().getDetail().getAddress());
		entity.setDocument(entity.getUser().getDetail().getDocument());
	}

}
