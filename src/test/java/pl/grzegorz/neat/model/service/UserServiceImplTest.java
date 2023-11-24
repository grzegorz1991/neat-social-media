package pl.grzegorz.neat.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.grzegorz.neat.model.entity.RoleEntity;
import pl.grzegorz.neat.model.entity.UserEntity;
import pl.grzegorz.neat.model.repository.RoleRepository;
import pl.grzegorz.neat.model.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(userRepository).isNotNull();
        assertThat(roleRepository).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(userService).isNotNull();
    }
    @Test
    void createUser() {
        // Arrange
        UserEntity user1 = new UserEntity();
        user1.setUsername("testUser");
        user1.setPassword("testPassword");

        RoleEntity role1 = new RoleEntity();
        role1.setName("ROLE_USER");
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role1));

        // Mock the password encoder
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        userService.createUser(user1, Collections.singletonList("ROLE_USER"));

        // Assert
        assertEquals(1, user1.getRoles().size());
        assertTrue(user1.getRoles().contains(role1));
        assertEquals("testUser", user1.getUsername());

        // Verify that userRepository.save is called with the expected user
        verify(userRepository).save(user1);

        // Verify that the password was encoded
        verify(passwordEncoder).encode("testPassword");
        assertNotEquals("testPassword", user1.getPassword());
        assertEquals("encodedPassword", user1.getPassword());
    }

    @Test
    void getUserByUsername() {
        // Arrange
        String testUsername = "testUser";
        UserEntity user = new UserEntity();
        user.setUsername(testUsername);

        // Mocking the UserRepository behavior
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        UserEntity result = userService.getUserByUsername(testUsername);

        // Assert
        assertEquals(testUsername, result.getUsername());
    }

    @Test
    void updateUser() {
        //Arrange
        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setUsername("Test User");
        existingUser.setEmail("test@email.com");

        UserEntity updatedUser = existingUser;
        updatedUser.setUsername("New Test User");
        updatedUser.setEmail("new@email.com");

        // Mocking the UserRepository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsernameAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        // Act
        UserEntity result = userService.updateUser(updatedUser);

        // Assert
        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getEmail(), result.getEmail());

        // Verify that userRepository.findById is called with the expected ID
        verify(userRepository).findById(1L);

        // Verify that userRepository.save is called with the expected user
        verify(userRepository).save(updatedUser);
    }
    @Test
    void deleteUser_UserExists() {
        // Arrange
        Long userId = 1L;
        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);

        // Mocking the UserRepository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        userService.deleteUser(userId);

        // Verify that userRepository.deleteById is called with the expected ID
        verify(userRepository).deleteById(userId);
    }
    @Test
    void deleteUser_UserNotExists() {
        // Arrange
        Long userId = 1L;

        // Mocking the UserRepository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUser(userId));

        assertEquals("User not found with ID: " + userId, exception.getMessage());

        // Verify that userRepository.deleteById is not called
        verify(userRepository, never()).deleteById(userId);
    }
    @Test
    void changePassword() {
        // Arrange
        Long userId = 1L;
        String newPassword = "newPassword";

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setUsername("testUser");
        existingUser.setPassword("oldPassword");

        // Mocking the UserRepository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        // Act
        userService.changePassword(userId, newPassword);

        // Assert
        assertEquals("encodedPassword", existingUser.getPassword());

        // Verify that userRepository.findById is called with the expected ID
        verify(userRepository).findById(userId);

        // Verify that userRepository.save is called with the expected user
        verify(userRepository).save(existingUser);

        // Verify that the password was encoded
        verify(passwordEncoder).encode(newPassword);
    }

}
