package com.vendido.vendido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vendido.vendido.entity.UserDetailEntity;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetailEntity, Long>{

}
