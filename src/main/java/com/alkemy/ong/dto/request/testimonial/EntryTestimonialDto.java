package com.alkemy.ong.dto.request.testimonial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Testimonials representation with the necessary data to edit or create a testimonials.")
public class EntryTestimonialDto {

    @NotBlank(message = "The Name field cannot be empty")
    @NotNull(message = "Field Name cannot be null")
    @Size(min = 2, max = 25, message = "The length of the Name must be between 2 to 25 characters.")
    @Schema(description = "The name that the testimonials will have.", example = "My testimonials",
    	maxLength = 25, minLength = 2)
    private String name;

    @Size(min = 2, max = 150, message = "The length of the Content must be between 2 to 25 characters.")
    @NotBlank(message = "The Name field cannot be empty")
    @Schema(description = "A content of this testimonials",
    	example = "This is my testimonials", nullable = true, maxLength = 150, minLength = 2)
    private String content;

}
