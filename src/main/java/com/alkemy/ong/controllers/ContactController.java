package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.ContactDto;
import com.alkemy.ong.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author nagredo
 * @project OT208-server
 * @class ContactController
 */
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;

    @PostMapping
    public ResponseEntity<ContactDto> create(@Valid @RequestBody ContactDto contactDto) {
        try {
            if (!contactDto.getName().isEmpty() || !contactDto.getEmail().isEmpty())
                return new ResponseEntity<>(this.service.saveContact(contactDto), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
