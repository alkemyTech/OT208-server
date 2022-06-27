package com.alkemy.ong.repositories;

import com.alkemy.ong.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    public boolean existsByFirstName(String firstName);

    public boolean existsByEmail(String email);

}
