package com.alkemy.ong.controllers;

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

    @PostMapping("/public/{id}")
    public ResponseEntity<OrganizationPublicDto> updateOrganization(@PathVariable String id,
                                                                    @RequestBody OrganizationPublicDto dto) {
        if (organizationService.existById(id)) {
            OrganizationPublicDto publicDataDTO = organizationService.updateOrganization(id, dto);
            return ResponseEntity.ok().body(publicDataDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}