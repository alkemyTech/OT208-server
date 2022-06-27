package com.alkemy.ong.services.impl;

import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.security.enums.RolName;
import com.alkemy.ong.services.ValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public String roleValidation(List<RoleEntity> role) {

        for (RoleEntity roleEntity : role)
            if (roleEntity.getRolName().equals(RolName.ROLE_USER)) {
                return "User " + roleEntity.getDescription();
            } else if (roleEntity.getRolName().equals(RolName.ROLE_ADMIN)) {
                return "Admin " + roleEntity.getDescription();
            }
        return "The user is not enabled";
    }
}
