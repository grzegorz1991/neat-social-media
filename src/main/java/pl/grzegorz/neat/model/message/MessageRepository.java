package pl.grzegorz.neat.model.message;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderAndReceiver(UserEntity sender, UserEntity receiver);
}
