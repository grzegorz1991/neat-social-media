package pl.grzegorz.neat.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.grzegorz.neat.model.message.MessageDTO;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.notification.NotificationEntity;
import pl.grzegorz.neat.model.notification.NotificationService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class NotificationController {


    private static final String GET_NOTIFICATIONS_FRAGMENT = "/home/notifications-fragment";

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping(GET_NOTIFICATIONS_FRAGMENT)
    public String getGetNotificationsFragment(Model model, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        List<NotificationEntity> notificationList = notificationService.getUnreadNotificationsForUser(currentUser.getId());
        setRelativeTime(notificationList);
        model.addAttribute("notificationList", notificationList);
        model.addAttribute("notificationSize", notificationList.size());
        return GET_NOTIFICATIONS_FRAGMENT;
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
    private void setRelativeTime(List<NotificationEntity> notifications) {
        for (NotificationEntity notification : notifications) {
            LocalDateTime timestamp = notification.getTimestamp();
            String relativeTime = convertToLocalDateTime(timestamp);
            notification.setRelativeTime(relativeTime);
        }
    }
}
