package pl.grzegorz.neat.model.service;

import pl.grzegorz.neat.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity createUser(UserEntity user, List<String> roles);
    UserEntity getUserByUsername( String username);
}
