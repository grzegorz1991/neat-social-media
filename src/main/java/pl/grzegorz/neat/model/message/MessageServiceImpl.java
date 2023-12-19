package pl.grzegorz.neat.model.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Page<MessageEntity> getMessagesForUser(int page, int pageSize, UserEntity user) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("timestamp").descending());
        return messageRepository.findAllByReceiver(pageable, user);
    }

    @Override
    public List<MessageEntity> getUnreadMessages(UserEntity user) {
        List<MessageEntity> messagesForUser = getMessagesForUser(user);
        List<MessageEntity> onlyUnreadMessages = new ArrayList<>();
        for (int i = 0; i < messagesForUser.size(); i++) {
            if (!(messagesForUser.get(i).isMessageRead())) {
                onlyUnreadMessages.add(messagesForUser.get(i));
            }
        }
        return onlyUnreadMessages;
    }

    @Override
    public int getNumberOfUnreadMessages(UserEntity user) {
        return getUnreadMessages(user).size();
    }

    @Override
    public MessageEntity getMessage(long id) {
        return messageRepository.getMessageEntityById(id);
    }

    @Override
    public void markMessageAsRead(long messageId) {
        Optional<MessageEntity> optionalMessage = messageRepository.findById(messageId);
        optionalMessage.ifPresent(message -> {
            message.setMessageRead(true);
            messageRepository.save(message);
        });
    }

    @Override
    public void archiveMessageBySender(long messageId) {
        Optional<MessageEntity> optionalMessage = messageRepository.findById(messageId);
        optionalMessage.ifPresent(message -> {
            message.setSenderArchived(true);
            messageRepository.save(message);
        });
    }

    @Override
    public void archiveMessageByReceiver(long messageId) {
        Optional<MessageEntity> optionalMessage = messageRepository.findById(messageId);
        optionalMessage.ifPresent(message -> {
            message.setRecipentArchived(true);
            messageRepository.save(message);
        });
    }

    @Override
    public Page<MessageEntity> getNonRecipentArchivedMessagesForUser(int page, int pageSize, UserEntity user) {
        return messageRepository.findNonArchivedMessagesForUser(user, PageRequest.of(page, pageSize));
    }

    @Override
    public Page<MessageEntity> getNonSenderArchivedMessagesForUser(int page, int pageSize, UserEntity user) {
        return messageRepository.findNonArchivedMessagesFromUser(user, PageRequest.of(page, pageSize));
    }

    @Override
    public Page<MessageEntity> getArchivedMessagesBySender(int page, int pageSize, UserEntity user) {
        return messageRepository.findArchivedMessagesBySender(user, PageRequest.of(page, pageSize));
    }


    @Override
    public Page<MessageEntity> getArchivedMessagesByReceiver(int page, int pageSize, UserEntity user) {
        return messageRepository.findArchivedMessagesByReceiver(user, PageRequest.of(page, pageSize));
    }

    @Override
    public List<MessageEntity> getTop5UnreadMessages(UserEntity user) {
        Page<MessageEntity> unreadMessagesPage = messageRepository.findTop5UnreadMessages(user, PageRequest.of(0, 5));
        return unreadMessagesPage.getContent();
    }

    @Override
    public List<MessageEntity> getAllNonArchivedMessagesByReceiver(UserEntity user) {
        return messageRepository.findAllNonArchivedMessagesByReceiver(user);
    }

    @Override
    public Page<MessageEntity> getAllNonArchivedMessagesBySender(int page, int pageSize,UserEntity user) {
        return messageRepository.findAllNonArchivedMessagesBySender(user, PageRequest.of(page, pageSize));
    }

    @Override
    public Page<MessageEntity> getArchivedMessagesByUser(int page, int pageSize, UserEntity user) {
        return messageRepository.findArchivedMessagesByUser(user, PageRequest.of(page, pageSize));
    }

}

