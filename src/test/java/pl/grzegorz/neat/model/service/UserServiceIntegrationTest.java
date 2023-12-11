package pl.grzegorz.neat.model.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pl.grzegorz.neat.model.role.RoleEntity;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.role.RoleRepository;
import pl.grzegorz.neat.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    @Order(0)
    void injectedComponentsAreNotNull(){
        assertThat(userRepository).isNotNull();
        assertThat(roleRepository).isNotNull();
    }

    @Test
    @Order(1)
    @Rollback(value = true)
    void createUser() {
        //Araange
        UserEntity user1 = new UserEntity();
        user1.setUsername("TestUser");
        user1.setPassword("TestPassword");
        user1.setEmail("Test@email.com");
        //Act
        userRepository.save(user1);

        //Assert
        System.out.println(user1.getId() + " - User test ID ");
        System.out.println(user1.getUsername() + " - User test name ");
        Assertions.assertThat(user1.getId()).isGreaterThan(0);
        System.out.println("User created: " + user1);
    }

    @Test
    @Order(2)
    void getUserByUsername() {
        //Araange
        UserEntity user1 = new UserEntity();
        user1.setUsername("TestUser");
        user1.setPassword("TestPassword");
        user1.setEmail("Test@email.com");

        //ACT
        userRepository.save(user1);
        UserEntity user2 = userRepository.findByUsername("TestUser").orElseThrow(()-> new RuntimeException("User not found with provided username"));

        //Assert
        Assertions.assertThat(user2.getId()).isGreaterThan(0);
        Assertions.assertThat(user2.getUsername()).isEqualTo("TestUser");
    }

    @Test
    @Order(3)
    void updateUser() {
        // Arrange
        UserEntity user1 = new UserEntity();
        user1.setUsername("TestUser");
        user1.setPassword("TestPassword");
        user1.setEmail("Test@email.com");
        userRepository.save(user1);

        // Act
        user1.setUsername("UpdatedUser");
        userRepository.save(user1);

        // Assert
        Optional<UserEntity> updatedUser = userRepository.findByUsername("UpdatedUser");
        assertTrue(updatedUser.isPresent());
        assertThat(updatedUser.get().getId()).isEqualTo(user1.getId());
    }

    @Test
    @Order(4)
    void deleteUser() {
        // Arrange
        UserEntity user1 = new UserEntity();
        user1.setUsername("TestUser");
        user1.setPassword("TestPassword");
        user1.setEmail("Test@email.com");
        UserEntity savedUser = userRepository.save(user1);

        // Act
        userRepository.deleteById(savedUser.getId());

        // Assert
        Optional<UserEntity> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }
    @Test
    @Order(5)
    @Transactional
    void updateUserRoles() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("TestUser");
        user.setPassword("TestPassword");
        user.setEmail("Test@email.com");
        userRepository.save(user);

        RoleEntity role1 = new RoleEntity();
        role1.setName("ROLE_USER");
        roleRepository.save(role1);

        RoleEntity role2 = new RoleEntity();
        role2.setName("ROLE_ADMIN");
        roleRepository.save(role2);

        List<String> newRoles = List.of("ROLE_USER", "ROLE_ADMIN");

        // Act
        userRepository.flush(); // Flush the changes to the database

        // Update user roles directly in the test
        UserEntity updatedUser = userRepository.getById(user.getId());
        updatedUser.getRoles().clear();
        newRoles.forEach(roleName -> {
            RoleEntity role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            updatedUser.getRoles().add(role);
        });
        userRepository.save(updatedUser);

        // Assert
        UserEntity reloadedUser = userRepository.getById(user.getId());
        assertThat(reloadedUser.getRoles()).extracting("name").containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }
    @Test
    @Order(6)
    void createDuplicateUsername() {
        // Arrange
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("ExistingUser");
        existingUser.setPassword("TestPassword");
        existingUser.setEmail("Test@email.com");
        userRepository.save(existingUser);

        UserEntity newUser = new UserEntity();
        newUser.setUsername("ExistingUser"); // Duplicate username
        newUser.setPassword("NewPassword");
        newUser.setEmail("Test@email.com");

        // Act and Assert
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(newUser),
                "Should throw DataIntegrityViolationException for duplicate username");
    }
    @Test
    @Order(7)
    void createUserWithoutEmail() {
        // Arrange
        UserEntity userWithoutEmail = new UserEntity();
        userWithoutEmail.setUsername("NoEmailUser");
        userWithoutEmail.setPassword("TestPassword");

        // Act and Assert
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(userWithoutEmail),
                "Should throw DataIntegrityViolationException for missing email");
    }

}