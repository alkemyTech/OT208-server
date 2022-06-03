package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.models.UserEntity;

import java.util.List;

public interface UserService extends BasicService<UserEntity, String> {

    UserEntity saveUser(UserRegisterDto userDTO);

    boolean deleteUser(String id);

    String login(UserLoginDto userLoginDto);

    UserRegisterDto updateUser(UserRegisterDto userRegisterDto, String id);

    List<UserRegisterDto> getAll();

}
