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
@Table(name = "order_details")
public class OrderDetailEntiy {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "address", length = 255)
	private String address;

	@Column(name = "document", length = 15)
	private String document;
	
	@OneToOne
	@JoinColumn(name = "order_id")
	private OrderEntity order;

}
