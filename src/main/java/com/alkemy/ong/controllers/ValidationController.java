package com.alkemy.ong.controllers;

import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.services.ValidationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

            boolean role = validationService.roleValidation(user.getRoleIds());

            if (role) {
                return new ResponseEntity<>("Usuario autorizado", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("El usuario requiere un rol permitido", HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    }

}
