package com.alkemy.ong.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.alkemy.ong.exeptions.RoleExistException;
import com.alkemy.ong.jwt.JwtUtils;
import com.alkemy.ong.models.RoleEntity;
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
    private final JwtUtils jwtUtils;

    public UserServiceImpl(IUserRepository repository, AuthenticationManager authenticationManager,
                           IUserRepository userRepository, PasswordEncoder passwordEncoder, IRoleRepository roleRepository,
                           JwtUtils jwtUtils) {
        super(repository);
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public String login(UserLoginDto userLoginDto) throws BadCredentialsException{

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(), userLoginDto.getPassword()));

        return this.getToken(authentication);
    }

    @Override
    public String singup(UserRegisterDto userRegisterDto) throws BadCredentialsException{

        this.saveUser(userRegisterDto);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userRegisterDto.getEmail(), userRegisterDto.getPassword()));

        return this.getToken(authentication);
    }

    private String getToken(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateToken(authentication);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

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

    private RoleEntity roleExist(RolName rolName) throws RoleExistException {
        if (roleRepository.findByRolName(rolName).isPresent())  {
            return roleRepository.findByRolName(rolName).get();
        } else {
            throw new RoleExistException(
                "Rol dont's exist:" + rolName);
        }
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
    	List<UserRegisterDto> usersDto =  ObjectMapperUtils.mapAll(this.findAll(), UserRegisterDto.class);
       if(usersDto.isEmpty()) {
    	   throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }
       else {
    	   return usersDto;
       }
    }

	@Override
	public boolean isAdmin(UserEntity user) {
		return user.getRoleIds().stream()
				.anyMatch(rol -> rol.getRolName().equals(RolName.ROLE_ADMIN));
	}

}
