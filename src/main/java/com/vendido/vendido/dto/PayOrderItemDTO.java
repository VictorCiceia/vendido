package com.vendido.vendido.dto;

import lombok.Data;

@Data
public class PayOrderItemDTO {
	
	private Long id;

	private long payOrderId;
	
	private long productId;
	
	private int productQuantity;
	
	private String productName;
	
	private int productPriceUnit;
	
	private int total;

}
