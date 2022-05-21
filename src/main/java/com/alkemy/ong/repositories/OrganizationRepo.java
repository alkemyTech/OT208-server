/*
 * 
 */
package com.alkemy.ong.repositories;

import com.alkemy.ong.models.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Adrian E. Camus <https://acamus79.github.io/>
 */
@Repository
public interface OrganizationRepo extends JpaRepository<OrganizationEntity, Long>{
    
}
