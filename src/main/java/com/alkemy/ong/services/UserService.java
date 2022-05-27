package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.payload.UserForm;

/**
 *
 * @author Adrian E. Camus <https://acamus79.github.io/>
 */
public interface UserService {

    UserEntity saveUser(UserRegisterDto userDTO);

    UserRegisterDto updateUser(UserForm userForm, String id);

}
