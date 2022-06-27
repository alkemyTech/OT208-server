package com.alkemy.ong.repositories;

import com.alkemy.ong.models.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrganizationRepository extends JpaRepository<OrganizationEntity, String> {

}
