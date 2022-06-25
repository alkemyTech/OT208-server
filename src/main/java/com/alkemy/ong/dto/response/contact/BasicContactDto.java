package com.alkemy.ong.dto.response.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicContactDto {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String message;

}
