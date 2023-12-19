package pl.grzegorz.neat.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.grzegorz.neat.model.message.MessageDTO;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.notification.NotificationEntity;
import pl.grzegorz.neat.model.notification.NotificationService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/get-unread-notifications-count")
    @ResponseBody
    public ResponseEntity<Integer> getUnreadNotificationsCount(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        int unreadNotificationsCount = notificationService.getUnreadNotificationsForUser(currentUser.getId()).size();

        return ResponseEntity.ok(unreadNotificationsCount);
    }

    @GetMapping("/get-latest-unread-notifications")
    public ResponseEntity<List<NotificationEntity>> getLatestUnreadNotifications(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser();

        List<NotificationEntity> latestUnreadNotifications = notificationService.getTop5UnreadNotifications(user);


        return ResponseEntity.ok(latestUnreadNotifications);
    }

}
