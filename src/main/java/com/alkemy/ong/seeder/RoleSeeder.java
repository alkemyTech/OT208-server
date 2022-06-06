package com.alkemy.ong.seeder;

import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.repositories.IRoleRepository;
import com.alkemy.ong.security.enums.RolName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RoleSeeder implements CommandLineRunner {
    @Autowired
    IRoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        RoleEntity rolAdmin = new RoleEntity(null, "hola", LocalDateTime.now(), RolName.ROLE_ADMIN);
        RoleEntity rolUser = new RoleEntity(null, "hola", LocalDateTime.now(), RolName.ROLE_USER);
        roleRepository.save(rolAdmin);
        roleRepository.save(rolUser);
    }
}
