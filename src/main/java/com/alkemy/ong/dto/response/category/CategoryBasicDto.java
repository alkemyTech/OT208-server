package com.alkemy.ong.dto.response.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Category representation with only the name as output data.")
public class CategoryBasicDto {
	
	@Schema(description = "Category name.", example = "Drama")
    private String name;

}
