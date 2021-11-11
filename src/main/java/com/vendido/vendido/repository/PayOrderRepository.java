package com.vendido.vendido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vendido.vendido.entity.PayOrderEntity;

@Repository
public interface PayOrderRepository extends JpaRepository<PayOrderEntity, Long>{
	
	public Page<PayOrderEntity> findAllByDeleted(final boolean deleted, final Pageable pageable);

	public Optional<PayOrderEntity> findByIdAndDeleted(final Long id, final boolean deleted);

}
