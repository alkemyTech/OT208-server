package com.alkemy.ong.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.exeptions.EmailNotSendException;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.services.EmailService;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl userServiceImpl;
	private final UserService userService;
	private final EmailService emailService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto) {
		try {
			if (userServiceImpl.findByEmail(userLoginDto.getEmail()).isPresent()) {
				userServiceImpl.login(userLoginDto);
				// return token
			} else
				return ResponseEntity.notFound().build();
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("false");
		}
		return ResponseEntity.ok("ok");
	}

	@PostMapping("/register")
	public ResponseEntity<UserEntity> signup(@RequestBody @Valid UserRegisterDto userDTO)
			throws EmailNotSendException, IOException {
//        userService.saveUser(userDTO);
//        Authentication auth;
//        auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
//        final String jwt = jwtTokenUtil.generateToken(auth);
//        return ResponseEntity.ok(new AuthResponseDTO(jwt));ww	
		UserEntity user = userService.saveUser(userDTO);
		emailService.sendEmailRegister(user.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
}