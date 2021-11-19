package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.PayOrderDTO;

import lombok.Data;

@Data
public class PayOrderResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<PayOrderDTO> list;
}
