package com.alkemy.ong.services;

import com.alkemy.ong.dto.ContactDto;
import com.alkemy.ong.models.ContactEntity;

import java.util.List;

public interface ContactService extends BasicService<ContactEntity, String> {
    ContactDto saveContact(ContactDto contactDto);

    List<ContactDto> getAllContacts();
}
