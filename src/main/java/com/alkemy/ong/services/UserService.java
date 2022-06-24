package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.models.UserEntity;

import java.io.IOException;
import java.util.List;

import java.util.Optional;

public interface UserService extends BasicService<UserEntity, String> {

    boolean deleteUser(String id);

    String logIn(UserLoginDto userLoginDto);

    BasicUserDto updateUser(UserRegisterDto userRegisterDto, String id);

    List<UserRegisterDto> getAll();

    String singUp(UserRegisterDto userRegisterDto) throws IOException;
    
    boolean isAdmin(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

}
