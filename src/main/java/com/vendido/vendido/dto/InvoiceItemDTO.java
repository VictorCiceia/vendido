package com.vendido.vendido.dto;

import lombok.Data;

@Data
public class InvoiceItemDTO {
	
	private Long id;

	private long invoiceId;
	
	private long productId;
	
	private int productQuantity;
	
	private String productName;

	private int productTaxPercentage;
	
	private int productCostUnit;
	
	private int productPriceUnit;
	
	private int total;
	
	private int taxTotal;
	
}
