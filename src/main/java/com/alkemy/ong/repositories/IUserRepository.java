package com.alkemy.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.models.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUserRepository extends JpaRepository<UserEntity, String> {
    
    @Query("SELECT u FROM UserEntity u where u.email = :email and u.soft_delete = false")
    Optional<UserEntity> findByEmail(@Param("email") String email);
	
}
