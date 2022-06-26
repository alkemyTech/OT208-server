package com.alkemy.ong.dto.request.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EntryCommentDto {

    @NotBlank(message = "The userId cannot be empty or null")
    @Size(max = 36, message = "The maximum size for the id is thirty six characters")
    @JsonProperty(value = "userId")
    private String userIdId;

    @NotBlank(message = "The body cannot be empty or null")
    @Size(max = 255, message = "The maximum size for the body is thirty six characters")
    private String body;

    @NotBlank(message = "The newsId cannot be empty or null")
    @Size(max = 36, message = "The maximum size for the id is thirty six characters")
    @JsonProperty(value = "newsId")
    private String newsIdId;
}
