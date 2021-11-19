package com.vendido.vendido.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionDTO implements BaseDTO{
	
	private static final long serialVersionUID = 1L;

	private String description;
}
