package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.organization.OrganizationRequestDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.services.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization")
@AllArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/public")
    public ResponseEntity<OrganizationPublicDto> publicData(@RequestParam String id) {
        OrganizationPublicDto publicDataDTO = organizationService.getPublicOrganizationData(id);
        return ResponseEntity.ok().body(publicDataDTO);
    }

    @PostMapping("/public")
    public ResponseEntity<OrganizationPublicDto> updateOrganization(@RequestBody OrganizationRequestDto dto) {
        if (organizationService.existById(dto.getId())) {
            OrganizationPublicDto publicDataDTO = organizationService.updateOrganization(dto);
            return ResponseEntity.ok().body(publicDataDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}