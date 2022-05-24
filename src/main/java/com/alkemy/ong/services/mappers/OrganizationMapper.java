package com.alkemy.ong.services.mappers;

import com.alkemy.ong.dto.OrganizationPublicDataDto;
import com.alkemy.ong.models.OrganizationEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationMapper {

    private final ModelMapper mapper;

    public OrganizationPublicDataDto publicDataOrganization(OrganizationEntity organizationEntity){
        return mapper.map(organizationEntity, OrganizationPublicDataDto.class);
    }

}
