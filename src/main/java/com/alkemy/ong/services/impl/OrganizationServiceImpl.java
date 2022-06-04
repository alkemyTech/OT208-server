package com.alkemy.ong.services.impl;

import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IUserRepository;
import org.springframework.stereotype.Service;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.mappers.OrganizationMapper;
import lombok.AllArgsConstructor;
import com.alkemy.ong.repositories.IOrganizationRepository;

@Service
public class OrganizationServiceImpl extends BasicServiceImpl<OrganizationEntity, String, IOrganizationRepository> implements OrganizationService {

    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(IOrganizationRepository repository, OrganizationMapper organizationMapper) {
        super(repository);
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationPublicDto getPublicOrganizationData(String id) {
        OrganizationEntity organization = super.findById(id).orElseThrow();
        return organizationMapper.publicDataOrganization(organization);
    }

    @Override
    public OrganizationPublicDto updateOrganization(String id, OrganizationPublicDto organizationPublicDto) {
        OrganizationEntity ong = super.findById(id).orElseThrow();
        ong.setPhone(organizationPublicDto.getPhone());
        ong.setImage(organizationPublicDto.getImage());
        ong.setName(organizationPublicDto.getName());
        ong.setAddress(organizationPublicDto.getAddress());
        super.save(ong);
        return organizationMapper.publicDataOrganization(ong);
    }

    @Override
    public boolean existById(String id) {
        return super.findById(id).isPresent();
    }

}
