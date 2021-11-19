package com.vendido.vendido.resource;

import java.util.List;

import com.vendido.vendido.dto.UserDTO;

import lombok.Data;

@Data
public class UserResource implements BaseResource{
	
	private static final long serialVersionUID = 1L;

	private List<UserDTO> list;
}
