package com.vendido.vendido.dto;

import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleDTO implements BaseDTO{
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String authority;

}