package com.alkemy.ong.repositories;

import com.alkemy.ong.models.TestimonialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nagredo
 * @project OT208-server
 * @class TestimonialsRepository
 */
@Repository
public interface ITestimonialsRepository extends JpaRepository<TestimonialsEntity, String> {
}
