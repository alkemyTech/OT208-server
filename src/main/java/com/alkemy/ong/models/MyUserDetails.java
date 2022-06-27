package com.alkemy.ong.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Data
public class MyUserDetails implements UserDetails {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String photo;
    private Collection<? extends GrantedAuthority> authorities;
    private List<String> credentials;

    public MyUserDetails(String id, String firstName, String lastName, String email, String password, String photo, Collection<? extends GrantedAuthority> authorities, List<String> credentials) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.authorities = authorities;
        this.credentials = credentials;
    }

    public static MyUserDetails build(UserEntity userEntity) {
        List<GrantedAuthority> authorities =
                userEntity.getRoleIds().stream().map(rol ->
                        new SimpleGrantedAuthority(rol.getRolName().name())).collect(Collectors.toList());
        List<String> credentials =
                userEntity.getRoleIds().stream().map(rol ->
                        rol.getRolName().name()).collect(Collectors.toList());
        return new MyUserDetails
                (userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getPassword(),
                        userEntity.getPhoto(), authorities, credentials);
    }

    public List<String> getCredentials() {
        return credentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}