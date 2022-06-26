package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.contact.EntryContactDto;
import com.alkemy.ong.dto.response.contact.BasicContactDto;
import com.alkemy.ong.exeptions.EmailNotSendException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.ContactService;
import com.alkemy.ong.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<BasicContactDto> create(
            @Valid @RequestBody EntryContactDto contactDto, Errors errors) throws EmailNotSendException, IOException {

        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());

        } else {
            emailService.sendEmailContactForm(contactDto.getEmail());
            return new ResponseEntity<>(this.service.saveContact(contactDto), HttpStatus.CREATED);
        }

    }

    @GetMapping
    public ResponseEntity<List<BasicContactDto>> getAll() {
        List<BasicContactDto> contacts = service.getAllContacts();
        if (contacts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
}
