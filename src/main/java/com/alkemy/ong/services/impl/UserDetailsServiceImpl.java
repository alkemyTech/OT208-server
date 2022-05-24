package com.alkemy.ong.services.impl;

import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IUserRepository;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " user not found."));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userEntity.getRoleIds())); // error because fix needed in "RoleEntity" field's name on UserEntity 

        return new User(userEntity.getFirstName(), userEntity.getPassword(), authorities);
    }

}
