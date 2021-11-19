package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.ProductDTO;

import lombok.Data;

@Data
public class ProductResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<ProductDTO> list;
	
}
