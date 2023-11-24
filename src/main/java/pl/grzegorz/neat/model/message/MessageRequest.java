package pl.grzegorz.neat.model.message;

import pl.grzegorz.neat.model.user.UserEntity;

public class MessageRequest {

    private UserEntity sender;
    private UserEntity receiver;
    private String content;

    public MessageRequest(UserEntity sender, UserEntity receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
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
}