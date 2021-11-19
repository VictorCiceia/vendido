package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.PayOrderItemDTO;

import lombok.Data;

@Data
public class PayOrderItemResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<PayOrderItemDTO> list;
	
}
