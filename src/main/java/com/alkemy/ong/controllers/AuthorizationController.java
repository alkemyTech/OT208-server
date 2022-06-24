package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/logIn")
    public ResponseEntity<String> logIn(@Valid @RequestBody UserLoginDto userLoginDto, Errors errors) throws Exception {
        try {
            if (errors.hasErrors()) {
                throw new ValidationException(errors.getFieldErrors());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.logIn(userLoginDto));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad password");
        }


    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody @Valid UserRegisterDto userRegisterDto, Errors errors) {
        try {
            if (errors.hasErrors()) {
                throw new ValidationException(errors.getFieldErrors());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.singUp(userRegisterDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<BasicUserDto> getMe(HttpServletRequest request) {
        String token = jwtUtils.getToken(request);
        String idUser = jwtUtils.extractId(token);
        UserEntity user = userService.findById(idUser).get();

        return ResponseEntity.ok(ObjectMapperUtils.map(user, BasicUserDto.class));
    }
}
