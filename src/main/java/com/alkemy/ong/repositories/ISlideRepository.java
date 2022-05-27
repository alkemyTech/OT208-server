package com.alkemy.ong.repositories;

import com.alkemy.ong.models.SlideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISlideRepository extends JpaRepository<SlideEntity, String> {
}
