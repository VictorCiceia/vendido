package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.CategoryDTO;

import lombok.Data;

@Data
public class CategoryResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;
	
	private List<CategoryDTO> list;

}
