package com.vendido.vendido.segurity.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.entity.UserEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.UserRepository;

@Service
public class UserJpaService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String email)  {
		
		UserEntity user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
	
		final List<GrantedAuthority> authorities = user.getRoles().stream() //
				.map(role -> new SimpleGrantedAuthority(role.getAuthority())) //
				.collect(Collectors.toList());

		return new User( //
				user.getEmail(), //
				user.getPassword(), //
				user.isEnabled(), //
				true, // accountNonExpired
				true, // credentialsNonExpired
				true, // accountNonLocked
				authorities //
		);
	}
}
