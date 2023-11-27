package pl.grzegorz.neat.model.message;


import pl.grzegorz.neat.model.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="sender_id", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name= "receiver_id" , nullable = false)
    private UserEntity receiver;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public MessageEntity() {
        this.timestamp = LocalDateTime.now();
    }

    @Column(nullable = false)
    private boolean messageRead;

    @Column(nullable = false)
    private String title;

    public MessageEntity(UserEntity sender, UserEntity receiver, String content, String title, boolean read) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.title = title;
        this.messageRead = read;
        this.timestamp = LocalDateTime.now();
    }

    public MessageEntity(UserEntity sender, UserEntity receiver, String content, LocalDateTime timestamp, boolean messageRead, String title) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.messageRead = messageRead;
        this.title = title;
    }

    public boolean isMessageRead() {
        return messageRead;
    }

    public void setMessageRead(boolean messageRead) {
        this.messageRead = messageRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
