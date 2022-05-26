package com.alkemy.ong.repositories;

import com.alkemy.ong.models.SlideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Eduardo Sanchez <https://github.com/EdwardDavys/>
 */

@Repository
public interface SlideRepository extends JpaRepository<SlideEntity,Long> {
}
