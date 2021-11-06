package com.vendido.vendido.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.vendido.vendido.dto.RoleDTO;
import com.vendido.vendido.service.RoleService;

@RestController
@RequestMapping("/api")
public class RoleController implements BaseController<RoleDTO> {

	@Autowired
	private RoleService roleService;

	@Override
	@GetMapping("/roles")
	public Page<RoleDTO> index(final Pageable pageable) {
		return this.roleService.findAll(pageable);
	}

	@Override
	@GetMapping("/roles/{id}")
	public RoleDTO find(@PathVariable final long id) throws Exception {
		return this.roleService.findById(id);
	}

	@Override
	@PostMapping("/roles")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public RoleDTO create(@Validated @RequestBody final RoleDTO dto) throws Exception {
		return this.roleService.save(dto);
	}

	@Override
	@PutMapping("/roles/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public RoleDTO update(@PathVariable final long id, @Validated @RequestBody final RoleDTO dto)
			throws Exception {
		return this.roleService.update(id, dto);
	}

	@Override
	@DeleteMapping("/roles/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleted(@PathVariable final long id) throws Exception {
		this.roleService.delete(id);
	}
}
