package pl.grzegorz.neat.model.service;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.entity.UserEntity;

import java.util.List;

@Service
public interface UserService {

    //assumes the existence of a user and focuses on role assignment or updates
    UserEntity createUser(UserEntity user, List<String> roles);

    UserEntity createUser(UserEntity user);

    UserEntity getUserByUsername(String username);

    UserEntity updateUser(UserEntity user);

    void deleteUser(Long userId);

    List<UserEntity> getAllUsers();

    void changePassword(Long userId, String newPassword);

    //associated with the creation of a new user, possibly involving additional steps related to user registration
    UserEntity registerUser(UserEntity user, List<String> roles);

    void updateUserRole(Long userId, List<String> roles);
}
