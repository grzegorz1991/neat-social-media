package pl.grzegorz.neat.model.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Service
public interface NotificationService {



    public Page<NotificationEntity> getUnreadNotificationsForUser(Long userId, Pageable pageable);

    public List<NotificationEntity> getUnreadNotificationsForUser(Long userId);
    Optional<NotificationEntity> getNotificationById(Long notificationId);

    NotificationEntity createNotification(NotificationEntity notification);
    NotificationEntity createFriendRequestNotification(UserEntity sender, UserEntity recipient);
    void markNotificationAsRead(Long notificationId);

    void deleteNotification(Long notificationId);

    NotificationEntity createGreetingNotification(UserEntity user);


    List<NotificationEntity> getTop5UnreadNotifications(UserEntity user);
}
