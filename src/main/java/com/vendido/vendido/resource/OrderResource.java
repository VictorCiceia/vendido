package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.OrderDTO;

import lombok.Data;

@Data
public class OrderResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<OrderDTO> list;
	
}
