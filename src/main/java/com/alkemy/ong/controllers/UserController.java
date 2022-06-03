package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.payloads.UserForm;
import com.alkemy.ong.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    public static final String DELETE_USER = "Usuario eliminado";
    public static final String NO_DELETE_USER = "Usuario no eliminado";
    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            return new ResponseEntity<>(this.userService.deleteUser(id) ? DELETE_USER : NO_DELETE_USER, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRegisterDto> update(@Valid @RequestBody UserForm userForm, @PathVariable String id) {
        try {
            UserRegisterDto userDTO = this.userService.updateUser(userForm, id);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserRegisterDto>> getAll(){
        try {
            return new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
}
