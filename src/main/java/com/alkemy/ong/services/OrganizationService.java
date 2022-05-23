package com.alkemy.ong.services;


import com.alkemy.ong.dto.OrganizationPublicDTO;

public interface OrganizationService {
    OrganizationPublicDTO getPublicOrganizationData(Long id);
}
