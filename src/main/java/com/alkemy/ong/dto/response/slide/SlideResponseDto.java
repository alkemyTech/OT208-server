package com.alkemy.ong.dto.response.slide;

import com.alkemy.ong.dto.response.Organization.OrganizationPublicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideResponseDto {

    private String id;
    private String imageUrl;
    private String text;
    private Integer order;

}
