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

    private String message;

    private boolean isRead;

    private LocalDateTime timestamp;

    public NotificationEntity() {
        this.timestamp = LocalDateTime.now();
        this.isRead = false;

    }

    public NotificationEntity(UserEntity sender, UserEntity recipient, FriendRequestStatus friendRequestStatus) {
        this();
        this.sender = sender;
        this.recipient = recipient;
        this.friendRequestStatus = friendRequestStatus;
        this.message = message;
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
    public enum FriendRequestStatus {
        PENDING, ACCEPTED, REJECTED
    }
}

