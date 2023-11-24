package pl.grzegorz.neat.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.grzegorz.neat.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<Object> findByUsernameAndIdNot(String username, Long id);

    Optional<Object> findByEmailAndIdNot(String email, Long id);
}
