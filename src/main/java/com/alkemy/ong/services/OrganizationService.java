package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;

public interface OrganizationService {

    OrganizationPublicDto getPublicOrganizationData(String id);

    OrganizationPublicDto updateOrganization(String id, OrganizationPublicDto organizationPublicDto);
}
