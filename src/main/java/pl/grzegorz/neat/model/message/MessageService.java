package pl.grzegorz.neat.model.message;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;

@Service
public interface MessageService {

    // MessageEntity sendMessage(UserEntity sender, UserEntity receiver, String content);

    MessageEntity sendMessage(UserEntity sender, UserEntity receiver, String content, String title, boolean isRead);

    List<MessageEntity> getMessages(UserEntity sender, UserEntity receiver);

    public List<MessageEntity> getMessagesForUser(UserEntity user);

    public List<MessageEntity> getMessagesFrom(UserEntity user);

    List<MessageEntity> getAllMessages();

    public Page<MessageEntity> getMessages(int page, int pageSize);

    Page<MessageEntity> getMessagesFromUser(int page, int pageSize, UserEntity user);

    Page<MessageEntity> getMessagesForUser(int page, int pageSize, UserEntity user);

    //  public List<MessageEntity> getlat5Messages();

    List<MessageEntity> getUnreadMessages(UserEntity user);

    public int getNumberOfUnreadMessages(UserEntity user);

    MessageEntity getMessage(long id);


    void markMessageAsRead(long messageId);

    void archiveMessageBySender(long messageId);

    void archiveMessageByReceipent(long messageId);

    public Page<MessageEntity> getNonRecipentArchivedMessagesForUser(int page, int pageSize, UserEntity user);


    Page<MessageEntity> getNonSenderArchivedMessagesForUser(int page, int pageSize, UserEntity user);

    Page<MessageEntity> getArchivedMessagesBySender(int page, int pageSize, UserEntity user);

    Page<MessageEntity> getArchivedMessagesByReceiver(int page, int pageSize, UserEntity user);

    List<MessageEntity> getTop5UnreadMessages(UserEntity user);
}
