package com.alkemy.ong.repositories;

import com.alkemy.ong.models.TestimonialsEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author nagredo
 * @project OT208-server
 * @class TestimonialsRepository
 */
public interface ITestimonialsRepository extends CrudRepository<TestimonialsEntity, Long> {
}
