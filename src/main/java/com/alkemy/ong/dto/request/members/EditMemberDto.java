package com.alkemy.ong.dto.request.members;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditMemberDto {

    @NotBlank(message = "The Name field cannot be empty")
    @NotNull(message = "Field Name cannot be null")
    @Size(min = 2, max = 25, message = "The length of the Name must be between 2 to 25 characters.")
    private String name;
    @Size(min = 5, max = 50, message = "The length of facebook url must be between 5 to 80 characters.")
    private String facebookUrl;
    @Size(min = 5, max = 50, message = "The length of instagram url must be between 5 to 80 characters.")
    private String instagramUrl;
    @Size(min = 5, max = 50, message = "The length of linkedin url must be between 5 to 80 characters.")
    private String linkedinUrl;
    @Size(min = 5, max = 50, message = "The length of description must be between 10 to 300 characters.")
    private String description;

}
