package com.alkemy.ong.dto.response.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BasicActivityDto {

    private String name;

    private String content;

    private String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime timestamps;
}
