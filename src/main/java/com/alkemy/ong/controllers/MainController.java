package com.alkemy.ong.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nagredo
 * @project OT208-server
 * @class MainController
 */
@RestController
@RequestMapping("/api")
public class MainController {

    @PostMapping(name = "login")
    public ResponseEntity<String> login(String rol) {
        if (rol != null && !rol.isEmpty()) {
            return new ResponseEntity<>(rol, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
    }
}
