package com.alkemy.ong.controllers;


import com.alkemy.ong.dto.AuthRequestDTO;
import com.alkemy.ong.dto.AuthResponseDTO;
import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.services.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Adrian E. Camus <https://acamus79.github.io/>
 */

@RestController
@RequestMapping("auth")
public class MainController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtTokenUtil;
            
    /**
     * SIGNUP
     * Registration method that generates a token and an automatic login.
     * @param userDTO
     * @return String jwt Token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO>signup(@RequestBody @Valid UserDTO userDTO){
        userService.saveUser(userDTO);
        Authentication auth;
        auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        final String jwt = jwtTokenUtil.generateToken(auth);
        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }
    /**
     * LOGIN
     * @param authRequest
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>login(@RequestBody @Valid AuthRequestDTO authRequest){
        Authentication auth;
        auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        final String jwt = jwtTokenUtil.generateToken(auth);
        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }
}
