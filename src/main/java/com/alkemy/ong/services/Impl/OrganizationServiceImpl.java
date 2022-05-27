package com.alkemy.ong.services.Impl;

import com.alkemy.ong.dto.OrganizationPublicDataDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import com.alkemy.ong.services.mappers.OrganizationMapper;
import com.alkemy.ong.services.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final IOrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public OrganizationPublicDataDto getPublicOrganizationData(String id) {
        OrganizationEntity organization = organizationRepository.findById(id).orElseThrow();
        return organizationMapper.publicDataOrganization(organization);
    }

}
