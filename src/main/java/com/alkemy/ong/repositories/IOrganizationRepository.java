package com.alkemy.ong.repositories;

import com.alkemy.ong.models.OrganizationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrganizationRepository extends JpaRepository<OrganizationEntity, String> {

    /**
     * Method to search only the records with the softDelete field set to false,
     *
     * @return List(Entity)
     */
    @Query("SELECT o FROM OrganizationEntity o WHERE o.softDelete = false")
    public List<OrganizationEntity> searchAllNonDeleted();

}
