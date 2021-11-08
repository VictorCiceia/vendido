package com.vendido.vendido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vendido.vendido.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	public Page<UserEntity> findAllByEnabled(final boolean enabled, final Pageable pageable);
	
	public Optional<UserEntity> findByEmail(final String email);

	public Optional<UserEntity> findByIdAndEnabled(final Long id, final boolean enabled);

}
