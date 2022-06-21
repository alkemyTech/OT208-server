package com.alkemy.ong.dto.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryCategoryDto {

    @NotBlank(message = "The name cannot be empty or null")
    @Pattern(regexp = "[a-zA-Z\\s]*", message = "The first Name cannot contain numbers or characters other than letters")
    @Size(max = 50, message = "The maximum size for the name is fifty characters" )
    private String name;


    @Size(max = 180, message = "The maximum size for the description is 180 characters" )
    private String description;

}
