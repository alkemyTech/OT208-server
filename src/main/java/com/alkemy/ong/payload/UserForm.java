package com.alkemy.ong.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nagredo
 * @project OT208-server
 * @class UserForm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String photo;
}
