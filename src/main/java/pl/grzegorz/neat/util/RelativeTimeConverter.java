package pl.grzegorz.neat.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class RelativeTimeConverter {
    public static String convertToLocalDateTime(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        long years = timestamp.until(now, ChronoUnit.YEARS);
        long months = timestamp.until(now, ChronoUnit.MONTHS);
        long days = timestamp.until(now, ChronoUnit.DAYS);
        long hours = timestamp.until(now, ChronoUnit.HOURS);
        long minutes = timestamp.until(now, ChronoUnit.MINUTES);
        long seconds = timestamp.until(now, ChronoUnit.SECONDS);

        if (years > 0) {
            return years + " years ago";
        } else if (months > 0) {
            return months + " months ago";
        } else if (days > 0) {
            return days + " days ago";
        } else if (hours > 0) {
            return hours + " hours ago";
        } else if (minutes > 0) {
            return minutes + " minutes ago";
        } else {
            return seconds + " seconds ago";
        }
    }


}