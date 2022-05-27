package com.alkemy.ong.repositories;

import com.alkemy.ong.models.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INewsRepository extends JpaRepository<NewsEntity, String> {
}
