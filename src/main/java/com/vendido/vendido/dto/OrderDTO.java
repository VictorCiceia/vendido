package com.vendido.vendido.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {
	
	private Long id;

	private int total;

	private int tax_total;

	private int productQuantity;
	
	private String status;

	private Date createdAt;
	
	private UserDTO user;
	
	private List<OrderItemDTO> items;
	

}
