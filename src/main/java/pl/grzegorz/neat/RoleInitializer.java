package pl.grzegorz.neat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.grzegorz.neat.model.role.RoleEntity;
import pl.grzegorz.neat.model.role.RoleRepository;

import javax.annotation.PostConstruct;

@Component
public class RoleInitializer {

    @Autowired
    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @PostConstruct
    private void initializeRoles() {
        // Check if roles exist, and initialize if not
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(createRole("ROLE_USER"));
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(createRole("ROLE_ADMIN"));
        }

    }

    private RoleEntity createRole(String roleName) {
        RoleEntity role = new RoleEntity();
        role.setName(roleName);
        return role;
    }
}
