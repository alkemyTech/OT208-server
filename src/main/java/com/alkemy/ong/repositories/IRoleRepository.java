package com.alkemy.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.models.RoleEntity;

public interface IRoleRepository extends JpaRepository<RoleEntity, String> {
	
}