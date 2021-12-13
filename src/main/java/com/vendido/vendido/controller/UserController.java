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

import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.resource.UserResource;
import com.vendido.vendido.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController implements BaseController<UserDTO, UserResource>{
	
	@Autowired
	private UserService userService;

	@Override
	@GetMapping("/users")
	public UserResource index(final Pageable pageable) {
		return this.userService.findAll(pageable);
	}
	
	@GetMapping("/users/search/{name}")
	public UserResource search(@PathVariable final String name, final Pageable pageable) throws Exception {
		return this.userService.searchByName(name, pageable);
	}
	
	@Override
	@GetMapping("/users/{id}")
	public UserDTO find(@PathVariable final long id) throws Exception {
		return this.userService.findById(id);
	}
	
	@GetMapping("/users/email/{email}")
	public UserDTO find(@PathVariable final String email) throws Exception {
		return this.userService.findByEmail(email);
	}

	@Override
	@PostMapping("/users")
	public UserDTO create(final UserDTO dto) throws Exception {
		return this.userService.save(dto);
	}

	@Override
	@PutMapping("/users/{id}")
	public UserDTO update(@PathVariable final long id, @Validated @RequestBody UserDTO dto) throws Exception {
		return this.userService.update(id, dto);
	}

	@Override
	@DeleteMapping("/users/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleted(final long id) throws Exception {
		this.userService.delete(id);		
	}

}
