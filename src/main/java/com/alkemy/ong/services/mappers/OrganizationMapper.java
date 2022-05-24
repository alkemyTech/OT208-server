package com.alkemy.ong.services.mappers;

import com.alkemy.ong.dto.OrganizationPublicDataDTO;
import com.alkemy.ong.models.OrganizationEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationMapper {

    private final ModelMapper mapper;

    public OrganizationPublicDataDTO publicDataOrganization(OrganizationEntity organizationEntity){
        return mapper.map(organizationEntity, OrganizationPublicDataDTO.class);
    }

}
