package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.OrderItemDTO;

import lombok.Data;

@Data
public class OrderItemResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<OrderItemDTO> list;
}
