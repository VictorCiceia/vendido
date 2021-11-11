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

import com.vendido.vendido.dto.CategoryDTO;
import com.vendido.vendido.service.CategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController implements BaseController<CategoryDTO>{
	
	@Autowired
	private CategoryService categoryService;

	@Override
	@GetMapping("/categories")
	public Page<CategoryDTO> index(final Pageable pageable) {
		return this.categoryService.findAll(pageable);
	}

	@Override
	@GetMapping("/categories/{id}")
	public CategoryDTO find(@PathVariable final long id) throws Exception {
		return this.categoryService.findById(id);
	}

	@Override
	@PostMapping("/categories")
	public CategoryDTO create(@Validated @RequestBody final CategoryDTO dto) throws Exception {
		return this.categoryService.save(dto);
	}

	@Override
	@PutMapping("/categories/{id}")
	public CategoryDTO update(@PathVariable final long id, @Validated @RequestBody final CategoryDTO dto) throws Exception {
		return this.categoryService.update(id, dto);
	}

	@Override
	@DeleteMapping("/categories/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleted(@PathVariable final long id) throws Exception {
		this.categoryService.delete(id);		
	}

}
