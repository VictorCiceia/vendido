package com.vendido.vendido.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class UserDTO implements BaseDTO{
	
	private static final long serialVersionUID = 11234L;
	
	private Long id;

	private String name;

	private String lastname;

	private String email;

	private Date createdAt;

	private String address;
	
	private String document;
	
	private String reference;
	
	private String phone;
	
	private String newPassword;
	
	private String oldPassword;

	private List<RoleDTO> roles;

}
