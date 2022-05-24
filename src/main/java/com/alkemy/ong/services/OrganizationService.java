package com.alkemy.ong.services;


import com.alkemy.ong.dto.OrganizationPublicDataDTO;

public interface OrganizationService {
    OrganizationPublicDataDTO getPublicOrganizationData(Long id);
}
