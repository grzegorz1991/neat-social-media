package pl.grzegorz.neat.model.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.grzegorz.neat.model.notification.NotificationService;
import pl.grzegorz.neat.model.role.RoleEntity;
import pl.grzegorz.neat.model.role.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final NotificationService notificationService;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
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
    public UserEntity getUserById(int id) {
        return userRepository.getById((long) id);
    }
    @Override
    public UserEntity getUserById(long id) {
        return userRepository.getById(id);
    }
    @Override
    public UserEntity updateUser(UserEntity user) {
        UserEntity existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + user.getId()));

        validateUpdate(user);

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());

        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
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

        return createUser(user, roles);

    }

    @Override
    public UserEntity registerUser(String username, String email, String password, String name, String surname) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setImagePath("/images/avatar/avatar1.png");


        // Clear existing roles
        user.getRoles().clear();

        // Set default role
        RoleEntity defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found: ROLE_USER"));
        user.getRoles().add(defaultRole);


        return userRepository.save(user);
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

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDTO> getAllUsersDTO() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(UserEntity user, UserEntity friend) {
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        userRepository.save(user);
        userRepository.save(friend);
    }

    @Override
    @Transactional
    public void removeFriend(UserEntity user, UserEntity friend) {
        try {
            // Remove the friend from the user's friend list
            UserEntity user1 = getUserById(user.getId());
            UserEntity friend1 = getUserById(friend.getId());

            user1.getFriends().remove(friend1);
            friend1.getFriends().remove(user1);

            userRepository.save(user1);
            userRepository.save(friend1);
          //  user.getFriends().remove(friend);
         //   userRepository.save(user);

            // Remove the user from the friend's friend list
          //  friend.getFriends().remove(user);
         //   userRepository.save(friend);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
        }
    }

    @Override
    public Set<UserEntity> getFriends(UserEntity user) {
        return user.getFriends();
    }

    @Override
    public boolean areFriends(UserEntity user, UserEntity friend) {
        return user.getFriends().contains(friend) && friend.getFriends().contains(user);
    }

    @Override
    public void updateUserLastSeen(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setLastSeen(LocalDateTime.now());
            user.setActive(true);
            userRepository.save(user);
        }
    }
}

