package pl.grzegorz.neat.model.user;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {

    //assumes the existence of a user and focuses on role assignment or updates
    UserEntity createUser(UserEntity user, List<String> roles);

    UserEntity createUser(UserEntity user);

    UserEntity getUserByUsername(String username);

    UserEntity getUserById(int id);

    UserEntity updateUser(UserEntity user);

    void deleteUser(Long userId);

    List<UserEntity> getAllUsers();

    void changePassword(Long userId, String newPassword);

    //associated with the creation of a new user, possibly involving additional steps related to user registration
    UserEntity registerUser(UserEntity user, List<String> roles);

    UserEntity registerUser(String username, String email, String password, String name, String surname);

    void updateUserRole(Long userId, List<String> roles);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserDTO> getAllUsersDTO();

    void addFriend(UserEntity user, UserEntity friend);

    void removeFriend(UserEntity user, UserEntity friend);

    Set<UserEntity> getFriends(UserEntity user);

    boolean areFriends(UserEntity user, UserEntity friend);

    void updateUserLastSeen(Long userId);

}
