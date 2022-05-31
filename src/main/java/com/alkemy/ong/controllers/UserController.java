package com.alkemy.ong.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    public static final String DELETE_USER = "Usuario eliminado";
    public static final String NO_DELETE_USER = "Usuario no eliminado";
    private final UserService userService;

    @DeleteMapping("user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            return new ResponseEntity<>(this.userService.deleteUser(id) ? DELETE_USER : NO_DELETE_USER, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
