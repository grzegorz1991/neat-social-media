package pl.grzegorz.neat.model.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderAndReceiver(UserEntity sender, UserEntity receiver);

    List<MessageEntity> findByReceiver(UserEntity user);

    List<MessageEntity> findBySender(UserEntity user);

    Page<MessageEntity> findAll(Pageable pageable);

    Page<MessageEntity> findAllBySender(Pageable pageable, UserEntity user);

    Page<MessageEntity> findAllByReceiver(Pageable pageable,UserEntity user);
    MessageEntity getMessageEntityById(Long id);

//    @Query("SELECT m FROM MessageEntity m WHERE m.user = ?1 AND m.recipentArchived = false ORDER BY m.timestamp DESC")
//    Page<MessageEntity> findNonArchivedMessagesForUser(UserEntity user, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.receiver = :user AND m.recipentArchived = false ORDER BY m.timestamp DESC")
    Page<MessageEntity> findNonArchivedMessagesForUser(UserEntity user, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.sender = :user AND m.senderArchived = false ORDER BY m.timestamp DESC")
    Page<MessageEntity> findNonArchivedMessagesFromUser(UserEntity user, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.sender = :user AND m.senderArchived = true ORDER BY m.timestamp DESC")
    Page<MessageEntity> findArchivedMessagesBySender(UserEntity user, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.receiver = :user AND m.recipentArchived = true ORDER BY m.timestamp DESC")
    Page<MessageEntity> findArchivedMessagesByReceiver(UserEntity user, Pageable pageable);
}
