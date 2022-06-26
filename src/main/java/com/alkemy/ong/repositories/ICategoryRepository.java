package com.alkemy.ong.repositories;

import com.alkemy.ong.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, String> {

    /**
     * Method to search only the records with the softDelete field set to false,
     *
     * @return List(Entity)
     */
    @Query("SELECT c FROM CategoryEntity c WHERE c.softDelete = false")
    public List<CategoryEntity> searchAllNonDeleted();

}
