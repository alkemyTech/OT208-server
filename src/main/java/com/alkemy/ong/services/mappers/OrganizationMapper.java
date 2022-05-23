package com.alkemy.ong.services.mappers;

import com.alkemy.ong.dto.OrganizationPublicDTO;
import com.alkemy.ong.entities.OrganizationEntity;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

    public OrganizationPublicDTO publicDataOrganization(OrganizationEntity organizationEntity){
        OrganizationPublicDTO organizationPublicDTO = new OrganizationPublicDTO();
        organizationPublicDTO.setName(organizationEntity.getName());
        organizationPublicDTO.setImage(organizationEntity.getImage());
        organizationPublicDTO.setPhone(organizationEntity.getPhone());
        organizationPublicDTO.setAddress(organizationEntity.getAddress());
        return organizationPublicDTO;
    }
}
