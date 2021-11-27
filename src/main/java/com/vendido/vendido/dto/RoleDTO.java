package com.vendido.vendido.dto;

import lombok.Data;

@Data
public class RoleDTO implements BaseDTO{
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String authority;

}