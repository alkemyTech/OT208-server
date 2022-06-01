package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.models.UserEntity;

public interface UserService extends BasicService<UserEntity, String> {

    UserEntity saveUser(UserRegisterDto userDTO);

    boolean deleteUser(String id);
}
