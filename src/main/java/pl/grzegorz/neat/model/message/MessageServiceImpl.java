package pl.grzegorz.neat.model.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public MessageEntity sendMessage(UserEntity sender, UserEntity receiver, String content) {
        MessageEntity message = new MessageEntity(sender, receiver, content);
        return messageRepository.save(message);
    }

    @Override
    public List<MessageEntity> getMessages(UserEntity sender, UserEntity receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }

    @Override
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }
}
