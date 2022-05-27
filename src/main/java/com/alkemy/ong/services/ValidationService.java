package com.alkemy.ong.services;

import com.alkemy.ong.models.RoleEntity;
import java.util.List;

public interface ValidationService {

    public boolean roleValidation(List<RoleEntity> role);
}
