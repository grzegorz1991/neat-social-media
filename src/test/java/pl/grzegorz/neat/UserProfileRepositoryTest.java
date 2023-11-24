package pl.grzegorz.neat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.grzegorz.neat.model.old.UserProfile;
import pl.grzegorz.neat.model.old.UserProfileRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileRepositoryTest {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    @Order(0)
    public void alwaysAsserted() {
        assertTrue(true);
    }

    // JUnit test for saveEmployee
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveUserProfileTest() {
        UserProfile userProfile = UserProfile.builder().userName("TestName").email("test@Mail").build();
        userProfileRepository.save(userProfile);
        Assertions.assertThat(userProfile.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getUserProfileTest() {
        UserProfile userProfile = userProfileRepository.findById(1L).get();
        System.out.println(userProfile.getUserName());
        Assertions.assertThat(userProfile.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getListOfUserProfilesTest() {
        List<UserProfile> employees = userProfileRepository.findAll();
        Assertions.assertThat(employees.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateUserProfileTest() {
        UserProfile userProfile = userProfileRepository.findById(1L).get();
        userProfile.setEmail("newTest@gmail.com");
        UserProfile updatedUserProfile = userProfileRepository.save(userProfile);
        Assertions.assertThat(updatedUserProfile.getEmail()).isEqualTo("newTest@gmail.com");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteEmployeeTest() {
        UserProfile userProfile = userProfileRepository.findById(1L).get();
        userProfileRepository.delete(userProfile);
        UserProfile userProfile1 = null;
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByEmail("newTest@gmail.com");
        if (optionalUserProfile.isPresent()) {
            userProfile1 = optionalUserProfile.get();
        }
        Assertions.assertThat(userProfile1).isNull();
    }
}