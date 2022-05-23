package com.alkemy.ong.services.Impl;

import com.alkemy.ong.dto.OrganizationPublicDTO;
import com.alkemy.ong.entities.OrganizationEntity;
import com.alkemy.ong.services.mappers.OrganizationMapper;
import com.alkemy.ong.repositories.OrganizationRepository;
import com.alkemy.ong.services.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public OrganizationPublicDTO getPublicOrganizationData(Long id) {
        OrganizationEntity organization = organizationRepository.getById(id);
        return organizationMapper.publicDataOrganization(organization);
    }
}
