package com.alkemy.ong.dto.request.contact;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EntryContactDto {

    @NotBlank(message = "The Name field cannot be empty")
    @NotNull(message = "Field Name cannot be null")
    @Size(min = 2, max = 25, message = "The length of the Name must be between 2 to 25 characters.")
    private String name;

    private String phone;

    @NotBlank(message = "The field must not be empty.")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Must be a properly formatted email address.")
    private String email;

    @Size(min = 4, max = 255, message = "The length of the Name must be between 4 to 255 characters.")
    private String message;

}
