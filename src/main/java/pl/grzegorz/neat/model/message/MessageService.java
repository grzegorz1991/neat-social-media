package pl.grzegorz.neat.model.message;

import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;

@Service
public interface MessageService {

   // MessageEntity sendMessage(UserEntity sender, UserEntity receiver, String content);

    MessageEntity sendMessage(UserEntity  sender, UserEntity receiver, String content, String title, boolean isRead);

    List<MessageEntity> getMessages(UserEntity sender, UserEntity receiver);
    public List<MessageEntity> getMessagesForUser(UserEntity user);

    public List<MessageEntity> getMessagesFrom(UserEntity user);
    List<MessageEntity> getAllMessages();
}
