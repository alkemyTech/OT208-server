package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.payload.UserForm;

public interface UserService {

    UserEntity saveUser(UserRegisterDto userDTO);

    UserRegisterDto updateUser(UserForm userForm, String id);

    boolean deleteUser(String id);

}
