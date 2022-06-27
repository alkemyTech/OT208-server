package com.alkemy.ong.dto.request.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Category representation with the necessary data to edit or create a category.")
public class EntryCategoryDto {

    @Schema(description = "The name that the category will have.", example = "Drama")
    @NotBlank(message = "The name cannot be empty or null")
    @Size(max = 50, message = "The maximum size for the name is fifty characters")
    private String name;

    @Schema(description = "A description of what this category represents",
            example = "This is a melodramatic type category", nullable = true)
    @Size(max = 50, message = "The maximum size for the name is fifty characters")
    private String description;

}
