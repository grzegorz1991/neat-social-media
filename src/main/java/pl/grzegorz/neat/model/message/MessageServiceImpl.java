package pl.grzegorz.neat.model.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }




    @Override
    public MessageEntity sendMessage(UserEntity sender, UserEntity receiver, String content, String title, boolean isRead) {
        MessageEntity message = new MessageEntity(sender, receiver, content, title, isRead);

        return messageRepository.save(message);
    }

    @Override
    public List<MessageEntity> getMessages(UserEntity sender, UserEntity receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }
    @Override
    public List<MessageEntity> getMessagesForUser(UserEntity user) {
        return messageRepository.findByReceiver(user);
    }

    @Override
    public List<MessageEntity> getMessagesFrom(UserEntity user) {
        return messageRepository.findBySender(user);
    }

    @Override
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Page<MessageEntity> getMessages(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return messageRepository.findAll(pageable);
    }
    @Override
    public Page<MessageEntity> getMessagesFromUser(int page, int pageSize, UserEntity user) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("timestamp").descending());
        return messageRepository.findAllBySender(pageable, user);
    }

    @Override
    public List<MessageEntity> getlat5Messages() {
        List<MessageEntity> list = messageRepository.findAll();

        // Step 1: Sort the list based on timestamp
        Collections.sort(list, Comparator.comparing(MessageEntity::getTimestamp));

        // Step 2: Create a new list to store the 5 messages with smallest timestamps
        List<MessageEntity> smallestTimestampMessages = new ArrayList<>();

        // Step 3: Add the first 5 messages to the new list
        for (int i = 0; i < Math.min(5, list.size()); i++) {
            smallestTimestampMessages.add(list.get(i));
        }

        // Step 4: Return the new list
        return smallestTimestampMessages;

    }

    @Override
    public List<MessageEntity> getUnreadMessages(UserEntity user) {
        List<MessageEntity> messagesForUser = getMessagesForUser(user);
        List<MessageEntity> onlyUnreadMessages = new ArrayList<>();
        for (int i = 0; i < messagesForUser.size(); i++) {
            if(!(messagesForUser.get(i).isMessageRead())){
                onlyUnreadMessages.add(messagesForUser.get(i));
            }
        }
        return onlyUnreadMessages;
    }
    @Override
    public int getNumberOfUnreadMessages(UserEntity user) {
        return getUnreadMessages(user).size();
    }
}
