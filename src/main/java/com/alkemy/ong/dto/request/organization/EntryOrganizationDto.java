package com.alkemy.ong.dto.request.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryOrganizationDto {

    @Size(min = 2, max = 30, message = "The length of the Name must be between 2 to 30 characters.")
    private String name;

    @URL(message = "Must be a properly formatted URL.")
    private String image;

    private Integer phone;

    @Email(message = "Must be a properly formatted email address.")
    private String email;

    @Size(min = 10, message = "Min 10 characters in address")
    private String address;

    @URL(message = "Must be a properly formatted URL.")
    @Size(max = 100, message = "Max 100 characters in facebookUrl")
    private String facebookUrl;

    @URL(message = "Must be a properly formatted URL.")
    @Size(max = 100, message = "Max 100 characters in linkedinUrl")
    private String instagramUrl;

    @URL(message = "Must be a properly formatted URL.")
    @Size(max = 100, message = "Max 100 characters in linkedinUrl")
    private String linkedinUrl;
}
