package com.alkemy.ong.repositories;

import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.security.enums.RolName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, String> {

    Optional<RoleEntity> findByRolName(RolName rolName);

}
