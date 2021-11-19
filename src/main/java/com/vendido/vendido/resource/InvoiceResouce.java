package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.InvoiceDTO;

import lombok.Data;

@Data
public class InvoiceResouce implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<InvoiceDTO> list;
	
}
