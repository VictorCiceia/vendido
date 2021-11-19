package com.vendido.vendido.dto;

import lombok.Data;

@Data
public class OrderItemDTO implements BaseDTO{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private long orderId;
	
	private int productQuantity;
	
	private ProductDTO product;

}
