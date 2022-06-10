package com.alkemy.ong.dto.request.activity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EntryActivityDto {

    @NotBlank
    @Size(max = 60, message = "The maximum size for the name is sixty characters")
    private String name;

    @NotBlank
    @Size(max = 1800, message = "The maximum size for the content is 255 characters")
    private String content;

}
