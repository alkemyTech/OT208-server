package com.alkemy.ong.repositories;

import com.alkemy.ong.security.enums.RolName;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.models.RoleEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, String> {

    Optional<RoleEntity> findByRolName(RolName rolName);

}
