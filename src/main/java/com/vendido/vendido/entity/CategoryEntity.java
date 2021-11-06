package com.vendido.vendido.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class CategoryEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category", length = 50)
	private String category;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	private Set<ProductEntity> products;

}
