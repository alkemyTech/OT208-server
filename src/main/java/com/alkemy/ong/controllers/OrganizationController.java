package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.organization.OrganizationRequestDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.SlideService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@AllArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

@GetMapping("/public")
public ResponseEntity<OrganizationPublicDto> publicData(@RequestParam String id) {
    if (organizationService.existById(id)) {
        OrganizationPublicDto publicDataDTO = organizationService.getPublicOrganizationData(id);
        return ResponseEntity.ok().body(publicDataDTO);
    }else return ResponseEntity.notFound().build();
}

    @PostMapping("/public")
    public ResponseEntity<OrganizationPublicDto> updateOrganization(@RequestBody OrganizationRequestDto dto) {
        if (organizationService.existById(dto.getId())) {
            return ResponseEntity.ok().body(organizationService.updateOrganization(dto));
        } else return ResponseEntity.notFound().build();
    }

}