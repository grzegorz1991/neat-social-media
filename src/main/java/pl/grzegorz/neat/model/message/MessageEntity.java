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

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String relativeTime;


    public MessageEntity() {
        this.timestamp = LocalDateTime.now();
    }

    @Column(nullable = false)
    private boolean messageRead;

    @Column(nullable = false)
    private boolean senderArchived;

    @Column(nullable = false)
    private boolean recipentArchived;

    @Column(nullable = false)
    private String title;

    public MessageEntity(UserEntity sender, UserEntity receiver, String content, String title, boolean read) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.title = title;
        this.messageRead = read;
        this.timestamp = LocalDateTime.now();
        this.recipentArchived = false;
        this.senderArchived = false;
    }

    public MessageEntity(UserEntity sender, UserEntity receiver, String content, LocalDateTime timestamp, boolean messageRead, String title) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.messageRead = messageRead;
        this.title = title;
        this.recipentArchived = false;
        this.senderArchived = false;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", title='" + title + '\'' +
                '}';
    }

    public boolean isSenderArchived() {
        return senderArchived;
    }

    public void setSenderArchived(boolean senderArchived) {
        this.senderArchived = senderArchived;
    }

    public boolean isRecipentArchived() {
        return recipentArchived;
    }

    public void setRecipentArchived(boolean recipentArchived) {
        this.recipentArchived = recipentArchived;
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

    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }
}
