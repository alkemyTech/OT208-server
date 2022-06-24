package com.alkemy.ong.services.impl;

import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.services.ValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean roleValidation(List<RoleEntity> role) {

        boolean flag = false;

        for (RoleEntity roleEntity : role) {

            if (roleEntity.getRolName().equals("USER") || roleEntity.getRolName().equals("ADMIN")) {
                flag = true;
            }
        }
        return flag;
    }
}
