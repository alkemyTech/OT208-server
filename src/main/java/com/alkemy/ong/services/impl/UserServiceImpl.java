package com.alkemy.ong.services.impl;

import java.util.Arrays;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.exeptions.EmailExistsException;
import com.alkemy.ong.exeptions.RoleExistException;
import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IRoleRepository;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.services.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService { 

    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    public UserDetails login(UserLoginDto userLoginDto) throws BadCredentialsException{
        UserDetails userDetails;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(), userLoginDto.getPassword()));
        //userDetails necesario para implementar JWT en este mismo metodo.
        userDetails = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //retornar el token
        return userDetails;
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 
     * @param userDTO
     * @return UserEntity Object
     * @throws EmailExistsException 
     */
    @Override
    public UserEntity saveUser(UserRegisterDto userDTO) throws EmailExistsException {

        if (emailExist(userDTO.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress:" + userDTO.getEmail());
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setRoleIds(Arrays.asList(roleExist("USER")));
        userEntity = this.userRepository.save(userEntity);

        return userEntity;
    }

    /**
     * Method to delete user
     * @param id
     * @return
     */
    @Override
    public boolean deleteUser(String id) {
        if (id != null) {
            this.userRepository.deleteById(id);

            return true;
        }

        return false;
    }

    /**
     * 
     * @param email
     * @return true/false
     */
    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    /**
     * 
     * @param role
     * @return RoleEntity Object
     * @throws RoleExistException 
     */
    private RoleEntity roleExist(String role) throws RoleExistException {

        if (roleRepository.findByName(role).isPresent())  {
            return roleRepository.findByName("USER").get();
        } else {
            throw new RoleExistException(
                "Rol dont's exist:" + role);
        }
    }
}
