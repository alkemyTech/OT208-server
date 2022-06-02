package com.alkemy.ong.dto.response.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailDto {

    private String id;

    private String name;

    private String description;

    private String image;

    private LocalDateTime timestamps;

    private Boolean softDelete;
}
