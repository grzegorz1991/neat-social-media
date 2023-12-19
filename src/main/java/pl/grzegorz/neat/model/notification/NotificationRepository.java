package pl.grzegorz.neat.model.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {


    List<NotificationEntity> findAll();

    @Query("SELECT n FROM NotificationEntity n WHERE n.recipient.id = :userId AND n.isRead = false")
    List<NotificationEntity> findUnreadNotificationsForUser(@Param("userId") Long userId);


    @Query("SELECT n FROM NotificationEntity n WHERE n.recipient.id = :userId AND n.isRead = false")
    Page<NotificationEntity> findUnreadNotificationsForUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT m FROM NotificationEntity m WHERE m.recipient = :user AND m.isRead = false ORDER BY m.timestamp DESC")
    Page<NotificationEntity> findTop5UnreadMessages(UserEntity user, Pageable pageable);
}
