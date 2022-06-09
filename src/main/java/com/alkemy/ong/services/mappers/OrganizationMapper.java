package com.alkemy.ong.services.mappers;

import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.models.OrganizationEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationMapper {

    private final ModelMapper mapper;

    public OrganizationPublicDto entity2Dto(OrganizationEntity organizationEntity) {
        return mapper.map(organizationEntity, OrganizationPublicDto.class);
    }
    
    public OrganizationEntity dto2Entity(OrganizationEntity organizationEntity, OrganizationPublicDto organizationPublicDto) {
        mapper.map(organizationPublicDto, organizationEntity);
        return organizationEntity;
    }

}
