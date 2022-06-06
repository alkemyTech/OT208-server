package com.alkemy.ong.services;

import com.alkemy.ong.dto.ContactDto;

import java.util.List;

/**
 * @author nagredo
 * @project OT208-server
 * @class ContactService
 */
public interface ContactService {
    ContactDto saveContact(ContactDto contactDto);

    List<ContactDto> getAllContacts();
}
