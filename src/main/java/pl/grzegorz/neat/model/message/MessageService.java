package pl.grzegorz.neat.model.message;

import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;

@Service
public interface MessageService {

    MessageEntity sendMessage(UserEntity sender, UserEntity receiver, String content);
    List<MessageEntity> getMessages(UserEntity sender, UserEntity receiver);

    List<MessageEntity> getAllMessages();
}
