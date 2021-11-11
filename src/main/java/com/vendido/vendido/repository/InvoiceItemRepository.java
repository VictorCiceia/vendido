package com.vendido.vendido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vendido.vendido.entity.InvoiceItemEntity;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, Long>{

	public List<InvoiceItemEntity> findByInvoiceId(final Long invoiceId);
	
}
