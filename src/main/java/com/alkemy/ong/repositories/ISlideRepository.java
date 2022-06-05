package com.alkemy.ong.repositories;

import com.alkemy.ong.models.SlideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISlideRepository extends JpaRepository<SlideEntity, String> {

    @Query(nativeQuery = true, value = "SELECT max(orders) FROM slides s")
    Integer getMaxOrder();
}
