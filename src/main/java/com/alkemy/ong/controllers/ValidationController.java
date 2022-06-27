package com.alkemy.ong.controllers;

import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.services.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;
    private final IUserRepository userRepository;

    @PostMapping("/validation/{id}")
    public ResponseEntity<String> validation(@PathVariable String id) {

        Optional<UserEntity> op = userRepository.findById(id);

        if (op.isPresent()) {
            UserEntity user = op.get();
            return new ResponseEntity<>(validationService.roleValidation(user.getRoleIds()), HttpStatus.OK);
        }
        return new ResponseEntity<>("The user is not registered", HttpStatus.NOT_FOUND);
    }
}
