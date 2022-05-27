package com.alkemy.ong.dto.request.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserLoginDto {

    @Email(message = "Must be a properly formatted email address.")
    @NotEmpty(message = "The field must not be empty.")
    @Size(max = 255)
    private String email;

    @Size(min = 8, max = 15, message = "Min 8, max 15 characters in password")
    @NotEmpty(message = "The field must not be empty.")
    private String password;
}
