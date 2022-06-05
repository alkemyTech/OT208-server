package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.exeptions.EmailNotSendException;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.services.EmailService;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.impl.UserServiceImpl;
import com.alkemy.ong.services.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

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
    private final UserMapper userMapper;

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
    public ResponseEntity<UserEntity> signup(@RequestBody @Valid UserRegisterDto userDTO) throws EmailNotSendException, IOException {
//        userService.saveUser(userDTO);
//        Authentication auth;
//        auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
//        final String jwt = jwtTokenUtil.generateToken(auth);
//        return ResponseEntity.ok(new AuthResponseDTO(jwt));
        UserEntity user = userService.saveUser(userDTO);
        emailService.sendEmailRegister(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<BasicUserDto> getMe(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        //String idUser = jwtUtils.extractId(token);
        UserEntity user = userService.findById("1").get();

        return ResponseEntity.ok(userMapper.mapperUserEntityToBasicUserDto(user));
    }
}
