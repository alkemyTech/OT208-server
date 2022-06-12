package com.alkemy.ong.dto.request.slide;

import com.alkemy.ong.models.OrganizationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideRequestDto {

    @NotBlank(message = "An image in base64 must be specified.")
    private String imageUrl;

    @NotBlank(message = "A text must be provided.")
    private String text;

    private Integer order;

    @NotBlank(message = "An organization must be specified.")
    private String organizationId;
}
