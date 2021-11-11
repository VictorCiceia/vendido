package com.vendido.vendido.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
	
	private Long id;

	private long orderId;
	
	private int productQuantity;
	
	private ProductDTO product;

}
