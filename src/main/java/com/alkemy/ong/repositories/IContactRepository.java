package com.alkemy.ong.repositories;

import com.alkemy.ong.models.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nagredo
 * @project OT208-server
 * @class ContactRepository
 */
@Repository
public interface IContactRepository extends JpaRepository<ContactEntity, String> {
}
