package com.alkemy.ong.dto.response.testimonial;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Testimonials representation with output data.")
public class BasicTestimonialDTo {

	@Schema(description = "Id of the testimonials entity.", example = "528f22c3-1f9c-493f-8334-c70b83b5b885")
    private String id;

    @Schema(description = "The name that the testimonials will have.", example = "My testimonials")
    private String name;

    @Schema(description = "Url of the image belonging to the testimonials.", example = "myImage.jpg")
    private String image;

    @Schema(description = "A content of this testimonials", example = "This is my testimonials")
    private String content;

}
