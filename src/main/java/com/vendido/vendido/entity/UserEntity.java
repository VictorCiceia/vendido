package com.vendido.vendido.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class UserEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "lastname", length = 100)
	private String lastname;

	@Column(name = "email", length = 150)
	private String email;

	@Column(name = "password", length = 255)
	private String password;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "enabled")
	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<RoleEntity> roles;
	
	@OneToOne(mappedBy = "user")
	private UserDetailEntity detail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<OrderEntity> orders;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<InvoiceEntity> invoices;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<PayOrderEntity> payOrders;

}
