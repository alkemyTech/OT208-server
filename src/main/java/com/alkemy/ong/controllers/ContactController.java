package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.ContactDto;
import com.alkemy.ong.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;

    @PostMapping(value = "/list")
    public ResponseEntity<ContactDto> create(@Valid @RequestBody ContactDto contactDto) {
        try {
            if (!contactDto.getName().isEmpty() || !contactDto.getEmail().isEmpty())
                return new ResponseEntity<>(this.service.saveContact(contactDto), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAll() {
        try {
            return new ResponseEntity<>(this.service.getAllContacts(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
