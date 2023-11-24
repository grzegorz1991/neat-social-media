package pl.grzegorz.neat.model.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.grzegorz.neat.model.repository.RoleRepository;
import pl.grzegorz.neat.model.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;



    @Test
    void createUser() {
    }

    @Test
    void getUserByUsername() {
    }
}