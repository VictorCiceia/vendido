package com.vendido.vendido.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.vendido.vendido.dto.PayOrderDTO;
import com.vendido.vendido.service.PayOrderService;

@RestController
@RequestMapping("/api")
public class PayOderController implements BaseController<PayOrderDTO>{
	
	@Autowired
	private PayOrderService payOrderService;

	@Override
	@GetMapping("/pay-orders")
	public Page<PayOrderDTO> index(final Pageable pageable) {
		return this.payOrderService.findAll(pageable);
	}

	@Override
	@GetMapping("/pay-orders/{id}")
	public PayOrderDTO find(@PathVariable final long id) throws Exception {
		return this.payOrderService.findById(id);
	}

	@Override
	@PostMapping("/pay-orders")
	public PayOrderDTO create(@Validated @RequestBody final PayOrderDTO dto) throws Exception {
		return this.payOrderService.save(dto);
	}

	@Override
	@PutMapping("/pay-orders/{id}")
	public PayOrderDTO update(@PathVariable final long id, @Validated @RequestBody final PayOrderDTO dto) throws Exception {
		return this.payOrderService.update(id, dto);
	}

	@Override
	@DeleteMapping("/pay-orders/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleted(@PathVariable final long id) throws Exception {
		this.payOrderService.delete(id);		
	}

}
