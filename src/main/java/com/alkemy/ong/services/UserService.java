package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.models.UserEntity;

import java.util.List;

import java.util.Optional;

public interface UserService extends BasicService<UserEntity, String> {

    UserEntity saveUser(UserRegisterDto userDTO);

    boolean deleteUser(String id);

    String login(UserLoginDto userLoginDto);

    BasicUserDto updateUser(UserRegisterDto userRegisterDto, String id);

    List<UserRegisterDto> getAll();

    public Optional<UserEntity> getByEmail(String email);

    public boolean existByFirstName(String nombreUsuario);

    public boolean existsByEmail(String email);

    String singup(UserRegisterDto userRegisterDto);

}
