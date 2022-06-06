package com.alkemy.ong.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nagredo
 * @project OT208-server
 * @class ContactEntity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String message;

}
