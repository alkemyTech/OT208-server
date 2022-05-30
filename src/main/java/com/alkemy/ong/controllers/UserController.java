package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.payload.UserForm;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    public static final String DELETE_USER = "Usuario eliminado";
    public static final String NO_DELETE_USER = "Usuario no eliminado";
    private final UserServiceImpl userServiceImpl;
    private final UserService userService;

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

    @PutMapping("users/{id}")
    public ResponseEntity<UserRegisterDto> update(@Valid @RequestBody UserForm userForm, @PathVariable String id) {
        try {
            UserRegisterDto userDTO = this.userService.updateUser(userForm, id);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            return new ResponseEntity<>(this.userService.deleteUser(id) ? DELETE_USER : NO_DELETE_USER, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
