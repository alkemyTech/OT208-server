package com.alkemy.ong.repositories;

import com.alkemy.ong.models.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivityRepository extends JpaRepository<ActivityEntity, String> {
}
