package com.alkemy.ong.services.impl;

import java.util.Arrays;
import java.util.Optional;

import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.security.enums.RolName;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.exeptions.EmailExistsException;
import com.alkemy.ong.exeptions.RoleExistException;
import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IRoleRepository;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.services.UserService;

@Service
public class UserServiceImpl extends BasicServiceImpl<UserEntity, String, IUserRepository> implements UserService { 

    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    
    public UserServiceImpl(IUserRepository repository, AuthenticationManager authenticationManager,
			IUserRepository userRepository, PasswordEncoder passwordEncoder, IRoleRepository roleRepository, JwtUtils jwtUtils) {
		super(repository);
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
	}

    public String login(UserLoginDto userLoginDto) throws BadCredentialsException{

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(), userLoginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.generateToken(authentication);
    }

    @Override
    public Optional<UserEntity> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existByFirstName(String firstName){
        return userRepository.existsByFirstName(firstName);
    }

    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
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
        userEntity.setRoleIds(Arrays.asList(roleRepository.findByRolName(RolName.ROLE_USER).get()));
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
     * @param rolName
     * @return RoleEntity Object
     * @throws RoleExistException 
     */
    private RoleEntity roleExist(RolName rolName) throws RoleExistException {

        if (roleRepository.findByRolName(rolName).isPresent())  {
            return roleRepository.findByRolName(rolName).get();
        } else {
            throw new RoleExistException(
                "Rol dont's exist:" + rolName);
        }
    }
}
