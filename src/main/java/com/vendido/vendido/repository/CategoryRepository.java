package com.vendido.vendido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vendido.vendido.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
	
	public Page<CategoryEntity> findAllByDeleted(final boolean deleted, final Pageable pageable);

	public Optional<CategoryEntity> findByIdAndDeleted(final Long id, final boolean deleted);

}
