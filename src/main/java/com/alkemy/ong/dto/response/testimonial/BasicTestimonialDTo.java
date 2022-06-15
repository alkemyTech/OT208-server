package com.alkemy.ong.dto.response.testimonial;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicTestimonialDTo {

    private String id;

    private String name;

    private String image;

    private String content;

}
