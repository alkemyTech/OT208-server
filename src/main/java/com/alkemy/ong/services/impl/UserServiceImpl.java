package com.alkemy.ong.services.impl;

import com.alkemy.ong.dtos.request.UserLogindto;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl { // implements UserService

    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;

    public UserDetails login(UserLogindto userLoginDto) {
        UserDetails userDetails;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(), userLoginDto.getPassword()));
        userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails;

    }

    public Optional<UserEntity> findByEmail(String email) {
        userRepository.findByEmail(email);
        return null;
    }

}
