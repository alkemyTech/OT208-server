package com.alkemy.ong.dto.response.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representation of the output data of a category.")
public class CategoryDetailDto {

	@Schema(description = "Id of the category entity.", example = "528f22c3-1f9c-493f-8334-c70b83b5b885")
    private String id;

	@Schema(description = "Category name.", example = "Drama")
    private String name;

	@Schema(description = "A description of what this category represents.", example = "This is a melodramatic type category")
    private String description;

	@Schema(description = "Url of the image belonging to the category.", example = "myImage.jpg")
    private String image;

	@Schema(description = "Date the category was created.", example = "21-06-2022 20:24:36")
    private LocalDateTime timestamps;

	@Schema(description = "Indicates if a category is active or not.", example = "false")
    private Boolean softDelete;
}
