package com.alkemy.ong.services;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.payload.UserForm;

public interface UserService {

    UserEntity saveUser(UserRegisterDto userDTO);

    UserDTO updateUser(UserForm userForm, String id);
}
