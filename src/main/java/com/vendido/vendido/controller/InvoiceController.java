package com.vendido.vendido.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vendido.vendido.dto.InvoiceDTO;
import com.vendido.vendido.resource.InvoiceResouce;
import com.vendido.vendido.service.InvoiceService;

@RestController
@RequestMapping("/api")
public class InvoiceController implements BaseController<InvoiceDTO, InvoiceResouce> {
	
	@Autowired
	private InvoiceService invoiceService;

	@Override
	@GetMapping("/invoices")
	public InvoiceResouce index(final Pageable pageable) {
		return this.invoiceService.findAll(pageable);
	}

	@Override
	@GetMapping("/invoices/{id}")
	public InvoiceDTO find(@PathVariable final long id) throws Exception {
		return this.invoiceService.findById(id);
	}

	@Override
	@PostMapping("/invoices")
	public InvoiceDTO create(@Validated @RequestBody final InvoiceDTO dto) throws Exception {
		return this.invoiceService.save(dto);
	}

	@Override
	@PutMapping("/invoices/{id}")
	public InvoiceDTO update(@PathVariable final long id, @Validated @RequestBody final InvoiceDTO dto) throws Exception {
		return this.invoiceService.update(id, dto);
	}

	@Override
	@DeleteMapping("/invoices/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleted(@PathVariable final long id) throws Exception {
		this.invoiceService.delete(id);
	}

}
