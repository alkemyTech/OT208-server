package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.organization.EntryOrganizationDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;

public interface OrganizationService {

    OrganizationPublicDto getPublicOrganizationData();

    OrganizationPublicDto updateOrganization(EntryOrganizationDto dto);

}
