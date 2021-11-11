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

import com.vendido.vendido.dto.OrderDTO;
import com.vendido.vendido.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController implements BaseController<OrderDTO> {
	
	@Autowired
	private OrderService orderService;

	@Override
	@GetMapping("/orders")
	public Page<OrderDTO> index(Pageable pageable) {
		return this.orderService.findAll(pageable);
	}

	@Override
	@GetMapping("/orders/{id}")
	public OrderDTO find(@PathVariable final long id) throws Exception {
		return this.orderService.findById(id);
	}

	@Override
	@PostMapping("/orders")
	public OrderDTO create(@Validated @RequestBody final OrderDTO dto) throws Exception {
		return this.orderService.save(dto);
	}

	@Override
	@PutMapping("/orders/{id}")
	public OrderDTO update(@PathVariable final long id, @Validated @RequestBody final OrderDTO dto) throws Exception {
		return this.orderService.update(id, dto);
	}

	@Override
	@DeleteMapping("/orders/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleted(@PathVariable final long id) throws Exception {
		this.orderService.delete(id);		
	}

}
