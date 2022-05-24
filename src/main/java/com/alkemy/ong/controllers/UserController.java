package com.alkemy.ong.controllers;

import com.alkemy.ong.services.impl.UserServiceImpl;
import javax.validation.Valid; // this bug will be fixed when the validation dependency is uploaded
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

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

}
