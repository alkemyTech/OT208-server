package com.alkemy.ong.services.impl;

import com.alkemy.ong.common.RolesEnum;
import com.alkemy.ong.payload.RoleForm;
import com.alkemy.ong.services.ValidationService;
import org.springframework.stereotype.Service;

/**
 * @author nagredo
 * @project OT208-server
 * @class ValidationServiceImpl
 */
@Service
public class ValidationServiceImpl implements ValidationService {
    @Override
    public boolean roleValidation(RoleForm roleForm) {
        return roleForm.getRol().equals(RolesEnum.USER.name()) || roleForm.getRol().equals(RolesEnum.ADMIN.name());
    }
}
