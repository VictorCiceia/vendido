package com.vendido.vendido.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceDTO implements BaseDTO{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String document;

	private String address;
	
	private int total;

	private int tax_total;

	private String payMethodd;

	private Date createdAt;
	
	private UserDTO user;
	
	private List<InvoiceItemDTO> items;

}
