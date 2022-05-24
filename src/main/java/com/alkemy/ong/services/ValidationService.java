package com.alkemy.ong.services;

import com.alkemy.ong.payload.RoleForm;

/**
 * @author nagredo
 * @project OT208-server
 * @class ValidationService
 */
public interface ValidationService {
    boolean roleValidation(RoleForm role);
}
