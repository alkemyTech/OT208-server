package com.alkemy.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.models.UserEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    public boolean existsByFirstName(String firstName);

    public boolean existsByEmail(String email);

}
