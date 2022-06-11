package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.contact.EntryContactDto;
import com.alkemy.ong.dto.response.contact.BasicContactDto;
import com.alkemy.ong.models.ContactEntity;

import java.util.List;

public interface ContactService extends BasicService<ContactEntity, String> {

    BasicContactDto saveContact(EntryContactDto dto);

    List<BasicContactDto> getAllContacts();
}

