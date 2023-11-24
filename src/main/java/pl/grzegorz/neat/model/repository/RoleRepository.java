package pl.grzegorz.neat.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.grzegorz.neat.model.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
