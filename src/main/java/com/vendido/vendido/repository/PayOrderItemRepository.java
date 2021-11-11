package com.vendido.vendido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vendido.vendido.entity.PayOrderItemEntity;

@Repository
public interface PayOrderItemRepository extends JpaRepository<PayOrderItemEntity, Long> {
	
	public List<PayOrderItemEntity> findByPayOrderId(final Long payOrderId);

}
