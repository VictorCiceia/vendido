package com.vendido.vendido.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductDTO {
	
	private Long id;

	private String name;
	
	private String description;
	
	private int cost;
	
	private int price;
	
	private int amount;
	
	private int taxPercentage;
	
	private Date createdAt;

	private CategoryDTO category;
	
}
