package com.vendido.vendido.dto;

import lombok.Data;

@Data
public class PayOrderItemDTO implements BaseDTO{
	
	private static final long serialVersionUID = 1234234L;
	
	private Long id;

	private long payOrderId;
	
	private long productId;
	
	private int productQuantity;
	
	private String productName;
	
	private int productPriceUnit;
	
	private int total;

}
