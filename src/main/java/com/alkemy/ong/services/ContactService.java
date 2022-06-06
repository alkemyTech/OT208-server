package com.alkemy.ong.services;

import com.alkemy.ong.dto.ContactDto;

/**
 * @author nagredo
 * @project OT208-server
 * @class ContactService
 */
public interface ContactService {
    ContactDto saveContact(ContactDto contactDto);

}
