package com.vendido.vendido.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user_details")
public class UserDetailEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "address", length = 150)
	private String address;

	@Column(name = "document", length = 15)
	private String document;

	@Column(name = "reference", length = 100)
	private String reference;

	@Column(name = "phone", length = 20)
	private String phone;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

}
