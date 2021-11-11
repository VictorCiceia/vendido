package com.vendido.vendido.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PayOrderDTO {
	
	private Long id;
	
	private String name;
	
	private String document;

	private String address;
	
	private int total;

	private String status;

	private Date createdAt;
	
	private UserDTO user;
	
	private List<PayOrderItemDTO> items;

}
