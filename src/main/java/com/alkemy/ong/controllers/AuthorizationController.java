package com.alkemy.ong.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.exeptions.EmailNotSendException;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.services.EmailService;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.impl.UserServiceImpl;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;

/**
 * @author nagredo
 * @project OT208-server
 * @class AuthorizationController
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserServiceImpl userServiceImpl;
    private final UserService userService;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto) throws Exception {
        try {
            if (userServiceImpl.findByEmail(userLoginDto.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userServiceImpl.login(userLoginDto));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("false");
        }

    }

    /**
     * SIGNUP Registration method that generates a token and an automatic login.
     *
     * @param userDTO
     * @return String jwt Token
     * @throws IOException
     * @throws EmailNotSendException
     */
    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody @Valid UserRegisterDto userRegisterDto) throws EmailNotSendException, IOException {
        emailService.sendEmailRegister(userRegisterDto.getEmail());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userServiceImpl.singup(userRegisterDto));

    }

    @GetMapping("/me")
    public ResponseEntity<BasicUserDto> getMe(HttpServletRequest request) {
        String token = jwtUtils.getToken(request);
        String idUser = jwtUtils.extractId(token);
        UserEntity user = userService.findById(idUser).get();
        
        return ResponseEntity.ok(ObjectMapperUtils.map(user, BasicUserDto.class));
    }
}
