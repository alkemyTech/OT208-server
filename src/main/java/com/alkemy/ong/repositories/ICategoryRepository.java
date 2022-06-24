package com.alkemy.ong.repositories;

import com.alkemy.ong.models.CategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, String> {

    @Query("SELECT c FROM CategoryEntity c WHERE c.softDelete = false")
    public List<CategoryEntity> searchAllNonDeleted();

}
