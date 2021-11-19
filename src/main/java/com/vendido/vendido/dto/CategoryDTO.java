package com.vendido.vendido.dto;

import lombok.Data;

@Data
public class CategoryDTO implements BaseDTO{
	
	private static final long serialVersionUID = 15588L;

	private Long id;

	private String category;

}
