/*
 * 
 */
package com.alkemy.ong.services;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.models.UserEntity;

/**
 *
 * @author Adrian E. Camus <https://acamus79.github.io/>
 */
public interface UserService {
    
    UserEntity saveUser(UserDTO userDTO); 
    
}
