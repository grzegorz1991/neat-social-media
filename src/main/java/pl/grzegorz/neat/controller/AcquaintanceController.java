package pl.grzegorz.neat.controller;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pl.grzegorz.neat.model.notification.NotificationEntity;
import pl.grzegorz.neat.model.notification.NotificationService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class AcquaintanceController {
    private static final String NEW_ACQUAINTANCE_FRAGMENT = "/home/newacquaintance-fragment";

    private static final String SEE_ACQUAINTANCE_FRAGMENT = "/home/seeacquaintance-fragment";

    private static final String SEE_ACQUAINTANCE_PROFILE_DETAILS_FRAGMENT = "/home/acquintanceprofile-details-fragment.html";

    private final UserService userService;

    private final NotificationService notificationService;

    @Autowired
    public AcquaintanceController(UserService userService, NotificationService notificationService) {

        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping(SEE_ACQUAINTANCE_PROFILE_DETAILS_FRAGMENT)
    public String gerAcquintanceProfileDetails(@RequestParam(defaultValue = "0") int userId, Model model, Authentication authentication){


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        UserEntity acquintance = userService.getUserById(userId);


        model.addAttribute("acquaintance", acquintance);
        model.addAttribute("userId", userId);
        return SEE_ACQUAINTANCE_PROFILE_DETAILS_FRAGMENT;
    }

    @GetMapping(NEW_ACQUAINTANCE_FRAGMENT)
    public String getNewAcquaintanceFragment(Model model, Authentication authentication) {
        List<UserEntity> userList = userService.getAllUsers();
        //Remove your own user from the list
        setRelativeTime(userList);


        model.addAttribute("users", userList);
        return NEW_ACQUAINTANCE_FRAGMENT;
    }

    @GetMapping(SEE_ACQUAINTANCE_FRAGMENT)
    public String getSeeAcquaintanceFragment(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        Set<UserEntity> acquintanceSet = userService.getUserById(currentUser.getId()).getFriends();
        model.addAttribute("acquintance",acquintanceSet );

        System.out.println(acquintanceSet);

        return SEE_ACQUAINTANCE_FRAGMENT;
    }

    private void setRelativeTime(List<UserEntity> users) {
        for (UserEntity user : users) {
            String relativeTime = "never";

            if (user.getLastSeen() != null) {
                LocalDateTime timestamp = user.getLastSeen();
                relativeTime = convertToLocalDateTime(timestamp);

            }
            user.setRelativeTime(relativeTime);
        }
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<String> sendFriendRequest(@RequestBody Map<String, String> requestPayload,Authentication authentication) {
        String recipientId = requestPayload.get("recipientId");
        int acquintanceId = Integer.parseInt(recipientId);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        UserEntity acquintance = userService.getUserById(acquintanceId);
        notificationService.createFriendRequestNotification(currentUser, acquintance);
        return ResponseEntity.ok("Friend request sent successfully to user with ID: " + recipientId);
    }

    @PostMapping("/breakTies")
    public ResponseEntity<String> breakTiesWithAcquaintance(@RequestBody Map<String, String> requestPayload, Authentication authentication) {
        //getting the acquiantance entity
        String acquaintanceId = requestPayload.get("acquaintanceId");
        UserEntity acquaintance = userService.getUserById(Integer.parseInt(acquaintanceId));
        //getting current user entity
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        userService.removeFriend(currentUser, acquaintance);

        return ResponseEntity.ok(currentUser.getUsername() +"broke Ties  with: " + acquaintanceId + " name:" + acquaintance.getUsername());
    }


    @PostMapping("/acceptRequest")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody Map<String, String> requestPayload) {
        try {
            long notificationId = Long.parseLong(requestPayload.get("notificationId"));

            Optional<NotificationEntity> notificationOptional = notificationService.getNotificationById(notificationId);

            if (notificationOptional.isPresent()) {
                NotificationEntity notification = notificationOptional.get();

                UserEntity acquintance = notification.getRecipient();
                UserEntity user = notification.getSender();

                userService.addFriend(user, acquintance);
                notification.setFriendRequestStatus(NotificationEntity.FriendRequestStatus.ACCEPTED);

                notificationService.updateNotification(notification);

                String successMessage = "Friend request accepted successfully for notification ID: " + notificationId;
                return ResponseEntity.ok(successMessage);
            } else {
                String errorMessage = "Notification not found for ID: " + notificationId;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        } catch (NumberFormatException e) {
            // Handle the case when the provided notificationId is not a valid long
            String errorMessage = "Invalid notification ID format";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            // Handle other exceptions
            String errorMessage = "An error occurred while processing the request";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

}
