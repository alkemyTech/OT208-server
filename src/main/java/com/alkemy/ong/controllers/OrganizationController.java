package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.organization.EntryOrganizationDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.services.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/organization")
@AllArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/public")
    public ResponseEntity<OrganizationPublicDto> publicData() {
        OrganizationPublicDto dto = organizationService.getPublicOrganizationData();
        if (dto.getName() != null) {
            return ResponseEntity.ok().body(dto);
        } else return ResponseEntity.notFound().build();
    }

    @PostMapping("/public")
    public ResponseEntity<OrganizationPublicDto> updateOrganization(@Valid @RequestBody EntryOrganizationDto entryDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(organizationService.updateOrganization(entryDto));
    }

}