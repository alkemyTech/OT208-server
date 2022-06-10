package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.contact.EntryContactDto;
import com.alkemy.ong.dto.response.contact.BasicContactDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;

    @PostMapping
    public ResponseEntity<BasicContactDto> create(@Valid @RequestBody EntryContactDto contactDto, Errors errors) {

        if (errors.hasErrors() || contactDto.getEmail().isEmpty() || contactDto.getName().isEmpty()) {
            throw new ValidationException(errors.getFieldErrors());
        } else return new ResponseEntity<>(this.service.saveContact(contactDto), HttpStatus.OK);

    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<ContactDto>> getAll() {
        try {
            return new ResponseEntity<>(this.service.getAllContacts(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
