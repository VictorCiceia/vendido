package com.vendido.vendido.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "pay_order_items")
public class PayOrderItemEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_quantity")
	private int productQuantity;

	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_price_unit")
	private int productPriceUnit;
	
	@Column(name = "total")
	private int Total;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pay_order_id")
	private PayOrderEntity payOrder;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private ProductEntity product;

}
