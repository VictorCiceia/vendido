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

import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController implements BaseController<ProductDTO>{
	
	@Autowired
	private ProductService productService;

	@Override
	@GetMapping("/products")
	public Page<ProductDTO> index(Pageable pageable) {
		return this.productService.findAll(pageable);
	}

	@Override
	@GetMapping("/products/{id}")
	public ProductDTO find(@PathVariable final long id) throws Exception {
		return this.productService.findById(id);
	}

	@Override
	@PostMapping("/products")
	public ProductDTO create(@Validated @RequestBody final ProductDTO dto) throws Exception {
		return this.productService.save(dto);
	}

	@Override
	@PutMapping("/products/{id}")
	public ProductDTO update(@PathVariable final long id, @Validated @RequestBody final ProductDTO dto) throws Exception {
		return this.productService.update(id, dto);
	}

	@Override
	@DeleteMapping("/products/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleted(@PathVariable final long id) throws Exception {
		this.productService.delete(id);	
	}

}
