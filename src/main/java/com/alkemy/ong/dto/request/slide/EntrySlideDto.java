package com.alkemy.ong.dto.request.slide;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrySlideDto {

    private Integer order;

    @NotBlank(message = "A text must be provided.")
    private String text;

    @NotBlank(message = "An image in base64 must be specified.")
    private String imageUrl;
}
