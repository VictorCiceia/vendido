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
@Table(name = "products")
public class ProductEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 255)
	private String name;
	
	@Column(name = "description", length = 255)
	private String description;
	
	@Column(name = "cost")
	private int cost;
	
	@Column(name = "price")
	private int price;
	
	@Column(name = "amount")
	private int amount;
	
	@Column(name = "tax_percentage")
	private int tax_percentage;
	
	@Column(name = "created_at")
	private Date created_at;
	
	@Column(name = "updated_at")
	private Date updated_at;	
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private CategoryEntity category;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	private Set<OrderItemEntity> orderItems;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	private Set<InvoiceItemEntity> invoiceItems;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	private Set<PayOrderItemEntity> payOrderItems;

}
