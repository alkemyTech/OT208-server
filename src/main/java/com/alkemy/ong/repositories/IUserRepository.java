package com.alkemy.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.models.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, String> {
	
}
