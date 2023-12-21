package pl.grzegorz.neat.model.notification;

import pl.grzegorz.neat.model.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserEntity recipient;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus friendRequestStatus;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String message;

    private boolean isRead;

    private LocalDateTime timestamp;

    private String relativeTime;

    public NotificationEntity() {
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    public NotificationEntity(UserEntity sender, UserEntity recipient, FriendRequestStatus friendRequestStatus, NotificationType notificationType) {
        this();
        this.sender = sender;
        this.recipient = recipient;
        this.friendRequestStatus = friendRequestStatus;
        this.notificationType = notificationType;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(UserEntity recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        this.isRead = read;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public FriendRequestStatus getFriendRequestStatus() {
        return friendRequestStatus;
    }

    public void setFriendRequestStatus(FriendRequestStatus friendRequestStatus) {
        this.friendRequestStatus = friendRequestStatus;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }

    public enum FriendRequestStatus {
        PENDING, ACCEPTED, REJECTED
    }

    public enum NotificationType {
        ALERT, INFORMATION, REQUEST, OTHER
    }


}
