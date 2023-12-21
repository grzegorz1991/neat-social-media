package pl.grzegorz.neat.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<Object> findByUsernameAndIdNot(String username, Long id);

    Optional<Object> findByEmailAndIdNot(String email, Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
    List<UserEntity> findAll();

    List<UserEntity> findByLastSeenGreaterThan(LocalDateTime thresholdTime);
}
