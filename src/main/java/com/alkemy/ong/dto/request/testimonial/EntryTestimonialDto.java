package com.alkemy.ong.dto.request.testimonial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryTestimonialDto {

    @NotBlank(message = "The Name field cannot be empty")
    @NotNull(message = "Field Name cannot be null")
    @Size(min = 2, max = 25, message = "The length of the Name must be between 2 to 25 characters.")
    private String name;

    @Size(min = 2, max = 150, message = "The length of the Content must be between 2 to 25 characters.")
    @NotBlank(message = "The Name field cannot be empty")
    private String content;

}
