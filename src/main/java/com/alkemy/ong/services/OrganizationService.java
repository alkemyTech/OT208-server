package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.organization.OrganizationRequestDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;

import java.util.List;

public interface OrganizationService {

    OrganizationPublicDto getPublicOrganizationData(String id);

    OrganizationPublicDto updateOrganization(OrganizationRequestDto dto);

    boolean existById(String id);

}
