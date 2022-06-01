package com.alkemy.ong.services.mappers;

import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.models.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper categoryMaper;

    public List<CategoryBasicDto> listCategories(List<CategoryEntity> categoryEntitys){
        List<CategoryBasicDto>categoryBasicDtos = new ArrayList<>();
        for (CategoryEntity categoryEntity:categoryEntitys){
            CategoryBasicDto categoryBasicDto = categoryMaper.map(categoryEntity,CategoryBasicDto.class);
            categoryBasicDtos.add(categoryBasicDto);
        }
        return categoryBasicDtos;
    }
}
