package com.alkemy.ong.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    private String id;

    @NotBlank(message = "The first Name field cannot be empty")
    @NotNull(message = "Field first Name cannot be null")
    @Pattern(regexp = "[a-zA-Z\\s]*", message = "The first Name cannot contain numbers or characters other than letters")
    @Size(min = 2, max = 25, message = "The length of the first Name must be between 2 to 25 characters.")
    private String firstName;

    @NotBlank(message = "The last Name field cannot be empty")
    @NotNull(message = "Field last Name cannot be null")
    @Pattern(regexp = "[a-zA-Z\\s]*", message = "The last Name cannot contain numbers or characters other than letters")
    @Size(min = 2, max = 25, message = "The length of the last Name must be between 2 to 25 characters.")
    private String lastName;

    @Email(message = "Must be a properly formatted email address.")
    @NotEmpty(message = "The field must not be empty.")
    private String email;

    @Size(min = 8, message = "Min 8 characters in password")
    @NotEmpty(message = "The field must not be empty.")
    private String password;

    private String photo;
}
