package pl.grzegorz.neat.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.grzegorz.neat.model.entity.RoleEntity;
import pl.grzegorz.neat.model.entity.UserEntity;
import pl.grzegorz.neat.model.repository.RoleRepository;
import pl.grzegorz.neat.model.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
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
    public UserEntity createUser(UserEntity user) {
        // Set default role or roles as needed
        List<String> defaultRoles = List.of("ROLE_USER");
        return createUser(user, defaultRoles);
    }
    @Override
    public UserEntity createUser(UserEntity user, List<String> roles) {

        //Assigning roles to new user
        user.getRoles().clear(); //clearing any possible role assigned to user

        roles.forEach(roleName -> {
            RoleEntity role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.getRoles().add(role);
        });

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }


    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
// Check if the user exists
        UserEntity existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + user.getId()));

        // Perform additional checks or validations before updating
        validateUpdate(user);


        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());


        return userRepository.save(existingUser);
    }

    private void validateUpdate(UserEntity updatedUser) {
        // Check if another user with the same username already exists
        userRepository.findByUsernameAndIdNot(updatedUser.getUsername(), updatedUser.getId())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("Username " + updatedUser.getUsername() + " is already taken.");
                });
        // Check if another user with the same email already exists
        userRepository.findByEmailAndIdNot(updatedUser.getEmail(), updatedUser.getId())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("Email " + updatedUser.getEmail() + " is already in use.");
                });
    }

    @Override
    public void deleteUser(Long userId) {
    // Check if the user exists
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        userRepository.deleteById(userId);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Encode the new password before updating
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    @Override
    public UserEntity registerUser(UserEntity user, List<String> roles) {
        // Implement the logic for user registration, similar to createUser
        // You may want to perform additional validations, send confirmation emails, etc.
        return createUser(user, roles);

    }


        @Override
        @Transactional
        public void updateUserRole(Long userId, List<String> roleNames) {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // Clear existing roles
            user.getRoles().clear();

            // Set new roles
            roleNames.forEach(roleName -> {
                RoleEntity role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                user.getRoles().add(role);
            });

            userRepository.save(user);
        }
    }

