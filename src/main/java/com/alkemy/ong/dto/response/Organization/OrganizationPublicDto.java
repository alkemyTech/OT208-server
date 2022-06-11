package com.alkemy.ong.dto.response.Organization;

import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPublicDto {

    private String name;
    private String image;
    private Integer phone;
    private String address;
    private String email;
    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;
    private List<SlideResponseDto> slides;
}
