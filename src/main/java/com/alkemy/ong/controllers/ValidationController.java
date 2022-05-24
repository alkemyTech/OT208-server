package com.alkemy.ong.controllers;

import com.alkemy.ong.payload.RoleForm;
import com.alkemy.ong.services.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nagredo
 * @project OT208-server
 * @class MainController
 */
@RestController
@RequestMapping("/api")
public class ValidationController {
    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("validation")
    public ResponseEntity<String> validation(@RequestBody RoleForm roleForm) {
        boolean role = validationService.roleValidation(roleForm);

        if (role)
            return new ResponseEntity<>("Usuario autorizado", HttpStatus.OK);
        else
            return new ResponseEntity<>("El usuario requiere un rol permitido", HttpStatus.FORBIDDEN);
    }
}
