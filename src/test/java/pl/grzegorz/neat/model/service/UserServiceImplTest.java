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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
    void createUser() {
        // Arrange
        UserEntity user1 = new UserEntity();
        user1.setUsername("testUser");
        user1.setPassword("testPassword");

        RoleEntity role1 = new RoleEntity();
        role1.setName("ROLE_USER");
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role1));

        // Act
        userService.createUser(user1, Collections.singletonList("ROLE_USER"));

        // Assert
        assertEquals(1, user1.getRoles().size());
        assertTrue(user1.getRoles().contains(role1));
        assertEquals("testUser", user1.getUsername());

        // Verify that userRepository.save is called with the expected user
        verify(userRepository).save(user1);
    }

    @Test
    void getUserByUsername() {

    }
}