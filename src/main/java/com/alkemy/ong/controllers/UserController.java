package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.payload.UserForm;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.impl.UserServiceImpl;
import javax.validation.Valid; // this bug will be fixed when the validation dependency is uploaded
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLogindto userLoginDto) throws Exception {
        try {
            if (userServiceImpl.findByEmail(userLoginDto.getEmail()) != null) {
                userServiceImpl.login(userLoginDto);
            }
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password.", e);
        }
        return null; // "return error {ok: false}" 
    }

    @PutMapping("users/{id}")
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserForm userForm, @PathVariable String id) {
        try {
            UserDTO userDTO = this.userService.updateUser(userForm, id);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
