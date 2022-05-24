/*
 * 
 */
package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.UserDTO;
import com.alkemy.ong.exeptions.EmailExistsException;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserServiceImpl implements UserService{
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity saveUser(UserDTO userDTO) throws EmailExistsException{
        
        if(emailExist(userDTO.getEmail())){
            throw new EmailExistsException(
            "There is an account with that email adress:" + userDTO.getEmail());
        }
               
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        //userEntity.setPhoto();
        //userEntity.setRole(); // Falta Resolver Rol
        userEntity = this.userRepository.save(userEntity);

        return userEntity;
    }
    
        private boolean emailExist(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    
    
}
