package com.vendido.vendido.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class OrderEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "total")
	private int total;

	@Column(name = "tax_total")
	private int taxTotal;

	@Column(name = "product_quantity")
	private int productQuantity;

	@Column(name = "status", length = 15)
	private String status;
	
	@Column(name = "created_at")
	private Date created_at;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	private Set<OrderItemEntity> orderItems;
	
	@OneToOne(mappedBy = "order")
	private OrderDetailEntiy detail;

}
