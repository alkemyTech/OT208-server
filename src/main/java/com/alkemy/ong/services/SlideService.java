package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.slide.SlideRequestDto;
import com.alkemy.ong.dto.request.slide.SlideUpdateDto;
import com.alkemy.ong.dto.response.slide.SlideDto;
import com.alkemy.ong.models.SlideEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SlideService extends BasicService<SlideEntity, String> {

    public List<SlideDto> getAll();

    public SlideDto getSlide(String id);

    SlideDto createSlide(SlideRequestDto dto);

    SlideDto updateSlide(String id, MultipartFile file);

}
