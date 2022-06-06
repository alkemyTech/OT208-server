package com.alkemy.ong.dto.request.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequestDto {

    private String id;
    private String name;
    private String image;
    private Integer phone;
    private String address;
}
