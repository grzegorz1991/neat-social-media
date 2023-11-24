package pl.grzegorz.neat.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.entity.RoleEntity;
import pl.grzegorz.neat.model.entity.UserEntity;
import pl.grzegorz.neat.model.repository.RoleRepository;
import pl.grzegorz.neat.model.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(UserEntity user, List<String> roles) {

        //Assigning roles to new user
        user.getRoles().clear(); //clearing any possible role assigned to user

        roles.forEach(roleName -> {
            RoleEntity role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.getRoles().add(role);
        });

//        // Encode the password before saving
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return null;
    }
}
