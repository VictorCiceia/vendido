package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.InvoiceItemDTO;

import lombok.Data;

@Data
public class InvoiceItemResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<InvoiceItemDTO> list;
}
