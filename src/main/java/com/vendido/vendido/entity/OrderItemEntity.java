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
@Table(name = "order_items")
public class OrderItemEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_quantity")
	private int productQuantity;

	@Column(name = "product_name", length = 255)
	private String productName;

	@Column(name = "product_tax_percentage")
	private int productTaxPercentage;
	
	@Column(name = "product_cost_unit")
	private int productCostUnit;
	
	@Column(name = "product_price_unit")
	private int productPriceUnit;
	
	@Column(name = "total")
	private int Total;
	
	@Column(name = "tax_total")
	private int taxTotal;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	private OrderEntity order;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private ProductEntity product;

}
