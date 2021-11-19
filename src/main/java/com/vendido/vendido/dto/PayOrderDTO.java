package com.vendido.vendido.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PayOrderDTO implements BaseDTO{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String document;

	private String address;
	
	private int total;

	private String status;

	private Date createdAt;
	
	private UserDTO user;
	
	private List<PayOrderItemDTO> items;

}
