package pl.grzegorz.neat.model.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Page<MessageEntity> findByRecipentArchivedFalseAndUserOrderByTimestampDesc(UserEntity user, Pageable pageable);
}
