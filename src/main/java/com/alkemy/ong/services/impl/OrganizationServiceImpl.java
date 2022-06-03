package com.alkemy.ong.services.impl;

import org.springframework.stereotype.Service;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.mappers.OrganizationMapper;
import lombok.AllArgsConstructor;
import com.alkemy.ong.repositories.IOrganizationRepository;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final IOrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public OrganizationPublicDto getPublicOrganizationData(String id) {
        OrganizationEntity organization = organizationRepository.findById(id).orElseThrow();
        return organizationMapper.publicDataOrganization(organization);
    }


    @Override
    public OrganizationPublicDto updateOrganization(String id, OrganizationPublicDto organizationPublicDto) {
        OrganizationEntity ong = organizationRepository.findById(id).orElseThrow();
        ong.setPhone(organizationPublicDto.getPhone());
        ong.setImage(organizationPublicDto.getImage());
        ong.setName(organizationPublicDto.getName());
        ong.setAddress(organizationPublicDto.getAddress());
        organizationRepository.save(ong);
        return organizationMapper.publicDataOrganization(ong);
    }

}
