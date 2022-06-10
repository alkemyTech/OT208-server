package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.organization.EntryOrganizationDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.SlideService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl extends BasicServiceImpl<OrganizationEntity, String, IOrganizationRepository> implements OrganizationService {

    private final SlideService slideService;

    public OrganizationServiceImpl(IOrganizationRepository repository, SlideService slideService) {
        super(repository);
        this.slideService = slideService;
    }

    @Override
    public OrganizationPublicDto getPublicOrganizationData() {
        OrganizationEntity organization = repository.findAll().get(0);
        List<SlideResponseDto> slides = slideService.getAllByOrganizationId(organization.getId());
        OrganizationPublicDto dto = ObjectMapperUtils.map(organization, OrganizationPublicDto.class);
        dto.setSlides(slides);
        return dto;
    }

    @Override
    public OrganizationPublicDto updateOrganization(EntryOrganizationDto dto) {
        OrganizationEntity ong = repository.findAll().get(0);
        ong = ObjectMapperUtils.map(dto, ong);
        save(ong);
        return ObjectMapperUtils.map(ong, OrganizationPublicDto.class);
    }

}
