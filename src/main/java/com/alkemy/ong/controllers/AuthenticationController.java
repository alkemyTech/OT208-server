package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;

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
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.impl.UserServiceImpl;
import com.alkemy.ong.services.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserServiceImpl userServiceImpl;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils	jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        try {
            if (userServiceImpl.findByEmail(userLoginDto.getEmail()).isPresent()) {
                userServiceImpl.login(userLoginDto);
                // return token
            }else
            	return ResponseEntity.notFound().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("false");
        }
        return ResponseEntity.ok("ok");
    }

    /**
     * SIGNUP Registration method that generates a token and an automatic login.
     *
     * @param userDTO
     * @return String jwt Token
     */
    @PostMapping("/register")
    public ResponseEntity<UserEntity> signup(@RequestBody @Valid UserRegisterDto userDTO) {
//        userService.saveUser(userDTO);
//        Authentication auth;
//        auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
//        final String jwt = jwtTokenUtil.generateToken(auth);
//        return ResponseEntity.ok(new AuthResponseDTO(jwt));
        UserEntity user = userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @GetMapping("/me")
    public ResponseEntity<BasicUserDto> getMe(HttpServletRequest request) {
    	String token = request.getHeader("Authorization");
    	String idUser = jwtUtils.extractId(token);
    	UserEntity user = userService.findById(idUser);
    	
    	return ResponseEntity.ok(userMapper.mapperUserEntityToBasicUserDto(user));
    }
}
