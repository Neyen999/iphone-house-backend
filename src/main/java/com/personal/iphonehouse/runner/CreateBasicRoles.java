package com.personal.iphonehouse.runner;

import com.personal.iphonehouse.enums.RolesEnum;
import com.personal.iphonehouse.model.Role;
import com.personal.iphonehouse.repositorie.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateBasicRoles implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        createBasicRoles();
    }

    @Transactional
    void createBasicRoles() {
        for (RolesEnum roleEnum : RolesEnum.values()) {
            if (!roleRepository.existsByName(roleEnum.name())) {
                Role role = new Role();
                role.setName(roleEnum.name());

                roleRepository.save(role);
            }
        }
    }
}
