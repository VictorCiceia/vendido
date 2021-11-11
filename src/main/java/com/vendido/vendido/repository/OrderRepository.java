package com.vendido.vendido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vendido.vendido.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>{
	
	public Page<OrderEntity> findAllByDeleted(final boolean deleted, final Pageable pageable);

	public Optional<OrderEntity> findByIdAndDeleted(final Long id, final boolean deleted);

}
