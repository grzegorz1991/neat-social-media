package pl.grzegorz.neat.model.message;

import pl.grzegorz.neat.model.user.UserEntity;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long messageId;
    private String title;
    private LocalDateTime timestamp;

    private String relativeTime;
    private UserEntity sender;

    public MessageDTO(Long id, String title, LocalDateTime timestamp, String relativeTime, UserEntity sender) {
        this.messageId = id;
        this.title = title;
        this.timestamp = timestamp;
        this.relativeTime = relativeTime;
        this.sender = sender;
    }



    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public MessageDTO(Long id, String title, LocalDateTime timestamp) {
        this.messageId = id;
        this.title = title;
        this.timestamp = timestamp;
    }


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
