package com.alkemy.ong.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.alkemy.ong.exeptions.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;

@Tag(name = "Authorization", description = "Endpoints to create authorisations for login, to register and to know the current roles of the users themselves.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/logIn")
    @Operation(summary = "Create login authorization",
            description = "Already registered users can access the application with the requested credentials. " +
                    "The method will return a time-limited token to carry encrypted and encoded user information " +
                    "in order to allow access on requests. Once the token expires, users will no longer have " +
                    "access to make requests and must renew it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in User"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden Access"),
            @ApiResponse(responseCode = "422", description = "Unprocessable User")})
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
    @Operation(summary = "Create register authorization",
            description = "Users can register with the appropriate requested credentials." +
                    "The method will return a time-limited token to carry encrypted and encoded " +
                    "user information in order to allow access on requests. Once the token expires, " +
                    "users will no longer have access to make requests and must renew it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signed Up User"),
            @ApiResponse(responseCode = "422", description = "Unprocessable User")})
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
    @PostMapping("/signUp")
    @Operation(summary = "Confirm roles",
            description = "Users can find out their own roles by using this method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "About Me")})
    public ResponseEntity<BasicUserDto> getMe(HttpServletRequest request) {
        String token = jwtUtils.getToken(request);
        String idUser = jwtUtils.extractId(token);
        UserEntity user = userService.findById(idUser).get();
        return ResponseEntity.ok(ObjectMapperUtils.map(user, BasicUserDto.class));
    }
}
