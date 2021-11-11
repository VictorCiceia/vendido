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
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "pay_orders")
public class PayOrderEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 255)
	private String name;
	
	@Column(name = "document", length = 15)
	private String document;
	
	@Column(name = "address", length = 255)
	private String address;
	
	@Column(name = "total")
	private int total;

	@Column(name = "status", length = 15)
	private String status;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "payOrder")
	private Set<PayOrderItemEntity> payOrderItems;

}
