package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.models.UserEntity;

public interface UserService {

    UserEntity saveUser(UserRegisterDto userDTO);

}
