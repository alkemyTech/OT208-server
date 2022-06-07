package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.organization.OrganizationRequestDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.SlideService;
import com.alkemy.ong.services.mappers.OrganizationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl extends BasicServiceImpl<OrganizationEntity, String, IOrganizationRepository> implements OrganizationService {

    private final OrganizationMapper organizationMapper;
    private final SlideService slideService;

    public OrganizationServiceImpl(IOrganizationRepository repository, OrganizationMapper organizationMapper, SlideService slideService) {
        super(repository);
        this.organizationMapper = organizationMapper;
        this.slideService = slideService;
    }

    @Override
    public OrganizationPublicDto getPublicOrganizationData(String id) {
        OrganizationEntity organization = repository.findById(id).orElseThrow();
        List<SlideResponseDto> slides = slideService.getAllByOrganizationId(id);
        OrganizationPublicDto dto = organizationMapper.entity2Dto(organization);
        dto.setSlides(slides);
        return dto;
    }

    @Override
    public OrganizationPublicDto updateOrganization(OrganizationRequestDto dto) {
        OrganizationEntity ong = repository.findById(dto.getId()).orElseThrow();
        ong.setPhone(dto.getPhone());
        ong.setImage(dto.getImage());
        ong.setName(dto.getName());
        ong.setAddress(dto.getAddress());
        super.save(ong);
        OrganizationPublicDto publicDataDTO = organizationMapper.entity2Dto(ong);
        return this.getPublicOrganizationData(dto.getId());
    }


}
