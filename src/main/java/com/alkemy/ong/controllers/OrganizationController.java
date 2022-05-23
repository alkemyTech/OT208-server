package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.OrganizationPublicDTO;
import com.alkemy.ong.services.OrgazationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("organization")
@AllArgsConstructor
public class OrganizationController {

    private final OrgazationService orgazationService;

    @GetMapping("/public")
    public ResponseEntity<OrganizationPublicDTO> publicData(@RequestParam Long id) {

        OrganizationPublicDTO publicDataDTO = orgazationService.getPublicOrgazationData(id);
        return ResponseEntity.ok().body(publicDataDTO);
    }
}

