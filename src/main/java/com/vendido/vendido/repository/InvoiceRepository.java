package com.vendido.vendido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vendido.vendido.entity.InvoiceEntity;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>{
	
	public Page<InvoiceEntity> findAllByDeleted(final boolean deleted, final Pageable pageable);

	public Optional<InvoiceEntity> findByIdAndDeleted(final Long id, final boolean deleted);

}
