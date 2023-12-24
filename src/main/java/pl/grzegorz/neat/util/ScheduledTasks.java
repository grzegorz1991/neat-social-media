package pl.grzegorz.neat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 300000) // Run every minute, adjust as needed
    public void updateLastSeen() {
        // Get users who logged in within the last 5 minutes
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(5);
        List<UserEntity> recentlyLoggedInUsers = userRepository.findByLastSeenGreaterThan(thresholdTime);

        // Update the active flag for users
        List<UserEntity> allUsers = userRepository.findAll();
        allUsers.forEach(user -> {
            boolean isActive = recentlyLoggedInUsers.contains(user);
            user.setActive(isActive);
            userRepository.save(user);
        });
        // Update the inactive flag for users who were not active
        allUsers.stream()
                .filter(user -> !recentlyLoggedInUsers.contains(user))
                .forEach(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                });
    }
}