package com.vendido.vendido.service;

import org.springframework.data.domain.Pageable;

public interface BaseService<DTO, Res> {

	public Res findAll(final Pageable pageable);

	public DTO findById(final long id) throws Exception;

	public DTO save(final DTO dto) throws Exception;

	public DTO update(final long id, final DTO dto) throws Exception;

	public void delete(final long id) throws Exception;
}
