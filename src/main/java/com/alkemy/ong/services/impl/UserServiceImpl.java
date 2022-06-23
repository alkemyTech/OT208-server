package com.alkemy.ong.services.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.alkemy.ong.exeptions.EmailNotSendException;
import com.alkemy.ong.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.alkemy.ong.dto.request.user.UserLoginDto;
import com.alkemy.ong.dto.request.user.UserRegisterDto;
import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.exeptions.ArgumentRequiredException;
import com.alkemy.ong.exeptions.EmailExistsException;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IRoleRepository;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.security.enums.RolName;
import com.alkemy.ong.services.UserService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;

@Service
public class UserServiceImpl extends BasicServiceImpl<UserEntity, String, IUserRepository> implements UserService {

    public static final String IS_REQUIRED_OR_DOESNT_EXIST = "El id del usuario es requerido o no existe";
    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(IUserRepository repository, AuthenticationManager authenticationManager, IUserRepository userRepository, PasswordEncoder passwordEncoder, IRoleRepository roleRepository, EmailService emailService, JwtUtils jwtUtils) {
        super(repository);
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public String logIn(UserLoginDto userLoginDto) throws BadCredentialsException {
        try {
            return this.getToken(userLoginDto.getEmail(), userLoginDto.getPassword());
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public String singUp(UserRegisterDto userRegisterDto) throws EmailNotSendException, IOException {

        if (this.repository.existsByEmail(userRegisterDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email already exists " + userRegisterDto.getEmail());
        }

        this.saveUser(userRegisterDto);
        emailService.sendEmailRegister(userRegisterDto.getEmail());
        return this.getToken(userRegisterDto.getEmail(), userRegisterDto.getPassword());
    }

    private String getToken(String email, String password) throws BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateToken(authentication);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void saveUser(UserRegisterDto userDTO) {
        UserEntity userEntity = ObjectMapperUtils.map(userDTO, UserEntity.class);
        userEntity.setRoleIds(List.of(roleRepository.findByRolName(RolName.ROLE_USER).get()));
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        this.userRepository.save(userEntity);

    }

    @Override
    public boolean deleteUser(String id) {
        if (id != null) {
            this.userRepository.deleteById(id);

            return true;
        }

        return false;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public BasicUserDto updateUser(UserRegisterDto userRegisterDto, String id) {
        if (this.findById(id).isPresent()) {
            userRegisterDto.setId(id);
            UserEntity userEntity = ObjectMapperUtils.map(userRegisterDto, UserEntity.class);

            userEntity = this.save(userEntity);
            return ObjectMapperUtils.map(userEntity, BasicUserDto.class);
        } else {
            throw new ArgumentRequiredException(IS_REQUIRED_OR_DOESNT_EXIST);
        }
    }

    @Override
    public List<UserRegisterDto> getAll() {
        List<UserRegisterDto> usersDto = ObjectMapperUtils.mapAll(this.findAll(), UserRegisterDto.class);
        if (usersDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return usersDto;
        }
    }

    @Override
    public boolean isAdmin(UserEntity user) {
        return user.getRoleIds().stream().anyMatch(rol -> rol.getRolName().equals(RolName.ROLE_ADMIN));
    }

}
