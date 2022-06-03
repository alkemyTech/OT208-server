package com.alkemy.ong.dto.response.Organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPublicDto {

    private String name;
    private String image;
    private Integer phone;
    private String address;
}
