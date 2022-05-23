package com.alkemy.ong.services.Impl;

import com.alkemy.ong.dto.OrganizationPublicDTO;
import com.alkemy.ong.services.mappers.OrganizationMapper;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.repositories.OrganizationRepository;
import com.alkemy.ong.services.OrgazationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrgazationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public OrganizationPublicDTO getPublicOrgazationData(Long id) {
        OrganizationEntity organization = organizationRepository.getById(id);
        return organizationMapper.publicDataOrganization(organization);
    }
}
