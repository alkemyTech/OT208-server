package com.alkemy.ong.repositories;

import com.alkemy.ong.models.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ActivityRepository extends JpaRepository<ActivityEntity,Long> {
}
