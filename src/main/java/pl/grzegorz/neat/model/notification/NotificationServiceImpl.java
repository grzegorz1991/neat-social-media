package pl.grzegorz.neat.model.notification;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl  implements NotificationService{
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<NotificationEntity> getUnreadNotificationsForUser(Long userId, Pageable pageable) {
        return notificationRepository.findUnreadNotificationsForUser(userId, pageable);
    }

    @Override
    public List<NotificationEntity> getUnreadNotificationsForUser(Long userId) {
        return notificationRepository.findUnreadNotificationsForUser(userId);
    }

    @Override
    public Optional<NotificationEntity> getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId);
    }

    @Override
    public NotificationEntity createNotification(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
        });
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public NotificationEntity createFriendRequestNotification(UserEntity sender, UserEntity recipient) {
        NotificationEntity notification = new NotificationEntity(sender, recipient, NotificationEntity.FriendRequestStatus.PENDING);
        return notificationRepository.save(notification);
    }

    @Override
    public NotificationEntity createGreetingNotification(UserEntity user) {
        System.out.println("New user Notification created");
        NotificationEntity notification = new NotificationEntity();

        notification.setRecipient(user);
        notification.setMessage("Welcome to NEAT!");
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }
    @Override
    public List<NotificationEntity> getTop5UnreadNotifications(UserEntity user) {
        Page<NotificationEntity> unreadNotifiationsPage = notificationRepository.findTop5UnreadMessages(user, PageRequest.of(0, 5));
        return unreadNotifiationsPage.getContent();
    }
}
