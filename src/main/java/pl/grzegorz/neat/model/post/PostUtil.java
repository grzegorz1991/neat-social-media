package pl.grzegorz.neat.model.post;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

public class PostUtil {

    private PostUtil() {
        // Private constructor to prevent instantiation
    }

    public static void setRelativeTime(List<PostEntity> posts) {
        for (PostEntity post : posts) {
            LocalDateTime timestamp = post.getCreatedAt();
            String relativeTime = convertToLocalDateTime(timestamp);
            post.setRelativeTime(relativeTime);
        }
    }

    public static List<PostEntity> filterOldPosts(List<PostEntity> posts, long maxAgeInMinutes) {
        List<PostEntity> filteredPosts = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (PostEntity post : posts) {
            LocalDateTime postTime = post.getCreatedAt();
            Duration duration = Duration.between(postTime, now);
            long minutesDifference = duration.toMinutes();

            // Filter out posts older than the specified age in minutes
            if (minutesDifference <= maxAgeInMinutes) {
                filteredPosts.add(post);
            }
        }

        return filteredPosts;
    }
}
