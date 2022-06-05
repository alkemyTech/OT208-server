package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.organization.OrganizationRequestDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.mappers.OrganizationMapper;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl extends BasicServiceImpl<OrganizationEntity, String, IOrganizationRepository> implements OrganizationService {

    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(IOrganizationRepository repository, OrganizationMapper organizationMapper) {
        super(repository);
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationPublicDto getPublicOrganizationData(String id) {
        OrganizationEntity organization = repository.findById(id).orElseThrow();
        return organizationMapper.publicDataOrganization(organization);
    }

    @Override
    public OrganizationPublicDto updateOrganization(OrganizationRequestDto dto) {
        OrganizationEntity ong = repository.findById(dto.getId()).orElseThrow();
        ong.setPhone(dto.getPhone());
        ong.setImage(dto.getImage());
        ong.setName(dto.getName());
        ong.setAddress(dto.getAddress());
        super.save(ong);
        return organizationMapper.publicDataOrganization(ong);
    }


}
