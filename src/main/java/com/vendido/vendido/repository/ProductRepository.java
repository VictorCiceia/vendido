package com.vendido.vendido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vendido.vendido.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	
	public Page<ProductEntity> findAllByDeleted(final boolean deleted, final Pageable pageable);

	public Optional<ProductEntity> findByIdAndDeleted(final Long id, final boolean deleted);

}
