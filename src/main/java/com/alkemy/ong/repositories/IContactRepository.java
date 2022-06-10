package com.alkemy.ong.repositories;

import com.alkemy.ong.models.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IContactRepository extends JpaRepository<ContactEntity, String> {
}
