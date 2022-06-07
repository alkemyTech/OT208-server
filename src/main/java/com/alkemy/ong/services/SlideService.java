package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.slide.SlideRequestDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.models.SlideEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SlideService extends BasicService<SlideEntity, String> {

    List<SlideResponseDto> getAll();

    List<SlideResponseDto> getAllByOrganizationId(String id);

    SlideResponseDto getSlide(String id);

    SlideResponseDto createSlide(SlideRequestDto dto);

    SlideResponseDto updateSlide(String id, MultipartFile file);

    Boolean deleteSlide(String id);

}
