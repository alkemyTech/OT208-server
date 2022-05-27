package com.alkemy.ong.services.impl;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " user not found."));
        
        Collection<SimpleGrantedAuthority> authorities = userEntity.getRoleIds().stream()
				.map(rol -> new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
        
        return new User(userEntity.getFirstName(), userEntity.getPassword(), authorities);
    }

}
