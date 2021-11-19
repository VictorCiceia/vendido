package com.vendido.vendido.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface BaseController<DTO, Res> {

	public Res index(final Pageable pageable);

	public DTO find(@PathVariable final long id) throws Exception;

	public DTO create(@Validated @RequestBody final DTO dto) throws Exception;

	public DTO update(@PathVariable final long id, @Validated @RequestBody final DTO dto) throws Exception;

	public void deleted(@PathVariable final long id) throws Exception;
}
