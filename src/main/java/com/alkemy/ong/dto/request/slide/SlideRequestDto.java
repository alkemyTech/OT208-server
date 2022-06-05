package com.alkemy.ong.dto.request.slide;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideRequestDto {

    private String imageUrl;
    private String text;
    private Integer order;
    private String organizationId;
}
