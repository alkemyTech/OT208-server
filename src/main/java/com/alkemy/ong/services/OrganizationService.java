package com.alkemy.ong.services;

import com.alkemy.ong.dto.OrganizationPublicDataDto;

public interface OrganizationService {

    OrganizationPublicDataDto getPublicOrganizationData(String id);
}
