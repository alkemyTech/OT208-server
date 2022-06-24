package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.organization.EntryOrganizationDto;
import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import com.alkemy.ong.services.OrganizationService;
import com.alkemy.ong.services.SlideService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
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
        List<OrganizationEntity> organizations = repository.findAll();
        if (organizations.size() > 0) {
            OrganizationEntity ong = organizations.get(0);
            OrganizationPublicDto dto = ObjectMapperUtils.map(ong, OrganizationPublicDto.class);
            dto.setSlides(slideService.getAllByOrganizationId(ong.getId()));
            return dto;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }
    }

    @Transactional
    @Override
    public OrganizationPublicDto updateOrganization(EntryOrganizationDto entryDto) {
        OrganizationEntity ong = repository.findAll().get(0);
        if(ong == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }
        save(ObjectMapperUtils.map(entryDto, ong));
        List<SlideResponseDto> slides = slideService.getAllByOrganizationId(ong.getId());
        OrganizationPublicDto dto = ObjectMapperUtils.map(ong, OrganizationPublicDto.class);
        dto.setSlides(slides);
        return dto;
    }

}
