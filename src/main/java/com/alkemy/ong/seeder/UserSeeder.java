package com.alkemy.ong.seeder;

import com.alkemy.ong.models.RoleEntity;
import com.alkemy.ong.models.UserEntity;
import com.alkemy.ong.repositories.IRoleRepository;
import com.alkemy.ong.repositories.IUserRepository;
import com.alkemy.ong.security.enums.RolName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private static final String PHOTO = "https://cohorte-mayo-2820e45d.s3.amazonaws.com/439df0793dfa45648365e4beeed292f4.png";
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity rolAdmin;
        RoleEntity rolUser;
        String password = passwordEncoder.encode("123456");

        if (roleRepository.count() < 2) {
            rolAdmin = new RoleEntity(null, "User with full privileges", LocalDateTime.now(), RolName.ROLE_ADMIN);
            rolUser = new RoleEntity(null, "User with restricted permissions", LocalDateTime.now(), RolName.ROLE_USER);
            roleRepository.save(rolAdmin);
            roleRepository.save(rolUser);
        } else {
            rolAdmin = roleRepository.findByRolName(RolName.ROLE_ADMIN).get();
            rolUser = roleRepository.findByRolName(RolName.ROLE_USER).get();
        }

        if (userRepository.count() == 0) {
            roles.add(rolAdmin);
            roles.add(rolUser);
            createAdmin(roles, password);
            roles.remove(rolAdmin);
            createUser(roles, password);
        }
    }

    private void createAdmin(List<RoleEntity> roles, String PASS) {
        UserEntity admin1 = new UserEntity(null, "Admin", "Root", "admin@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin1);
        UserEntity admin2 = new UserEntity(null, "Heidi", "Cannon", "heidicannon@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin2);
        UserEntity admin3 = new UserEntity(null, "Edward", "Wood", "edwardwood@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin3);
        UserEntity admin4 = new UserEntity(null, "Dustin", "Smith", "dustinsmith@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin4);
        UserEntity admin5 = new UserEntity(null, "Roberta", "Hahn", "robertahahn@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin5);
        UserEntity admin6 = new UserEntity(null, "Rose", "Baldwin", "rosebaldwin@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin6);
        UserEntity admin7 = new UserEntity(null, "Brooke", "Adkins", "brookeadkins@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin7);
        UserEntity admin8 = new UserEntity(null, "Jessica", "Hodge", "jessicahodge@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin8);
        UserEntity admin9 = new UserEntity(null, "Courtney", "Gill", "courtneygill@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin9);
        UserEntity admin10 = new UserEntity(null, "Stephen", "Morris", "stephenmorris@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(admin10);

    }

    private void createUser(List<RoleEntity> roles, String PASS) {
        UserEntity user1 = new UserEntity(null, "User", "User", "user@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user1);
        UserEntity user2 = new UserEntity(null, "Nancy", "Cooper", "nancycooper@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user2);
        UserEntity user3 = new UserEntity(null, "Christine", "Simpson", "christinesimpson@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user3);
        UserEntity user4 = new UserEntity(null, "Rachel", "Turner", "rachelturner@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user4);
        UserEntity user5 = new UserEntity(null, "Gerald", "Hunter", "geraldhunter@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user5);
        UserEntity user6 = new UserEntity(null, "Allison", "Proctor", "allisonproctor@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user6);
        UserEntity user7 = new UserEntity(null, "Julie", "Wilson", "juliewilson@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user7);
        UserEntity user8 = new UserEntity(null, "James", "Brooks", "jamesbrooks@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user8);
        UserEntity user9 = new UserEntity(null, "Sarah", "Barr", "sarahbarr@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user9);
        UserEntity user10 = new UserEntity(null, "Robert", "Morgan", "robertmorgan@mail.com", PASS, PHOTO, roles, LocalDateTime.now(), false);
        userRepository.save(user10);
    }

}
