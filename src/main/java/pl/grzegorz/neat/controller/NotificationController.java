package pl.grzegorz.neat.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.grzegorz.neat.model.message.ArchiveMessageDTO;
import pl.grzegorz.neat.model.message.MessageDTO;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.notification.NotificationDTO;
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

    // New endpoint to get notification content
//    @GetMapping("/home/get-notification-content/{notificationId}")
//    @ResponseBody
//    public ResponseEntity<String> getNotificationContent(@PathVariable Long notificationId) {
//        try {
//            // Replace the following line with your actual logic to get notification content
//            String notificationContent = notificationService.getNotificationById(notificationId).get().getMessage();
//            return ResponseEntity.ok()
//                    .contentType(MediaType.TEXT_PLAIN) // Set the content type to plain text
//                    .body(notificationContent);
//        } catch (Exception e) {
//            // Log the exception details for debugging
//            e.printStackTrace();
//
//            // Handle the exception, e.g., log it or return an error response
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving notification content");
//        }
//    }

    @GetMapping("/home/get-notification-content/{notificationId}")
    @ResponseBody
    public ResponseEntity<NotificationDTO> getNotificationContent(@PathVariable Long notificationId) {
        try {
            NotificationEntity notificationEntity = notificationService.getNotificationById(notificationId).orElseThrow();
            NotificationDTO notificationDTO = NotificationDTO.fromEntity(notificationEntity);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(notificationDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/home/set-notification-asread/{notificationId}")
    public ResponseEntity<Void> handleNotificationClick(
            @PathVariable Long notificationId,
            @RequestBody NotificationClickRequest request) {
        // For simplicity, just log that the notification is clicked
        System.out.println("Notification clicked for ID: " + notificationId);

        // You can perform additional actions based on the notificationId and request data

        return ResponseEntity.ok().build();
    }
    private static class NotificationClickRequest {
        private String type;
        private String message;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
