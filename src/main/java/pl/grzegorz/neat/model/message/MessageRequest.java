package pl.grzegorz.neat.model.message;

import pl.grzegorz.neat.model.user.UserEntity;

public class MessageRequest {


    private int receiverId;
    private String content;

    private String title;



    public MessageRequest( Integer receiverId, String content,  String title) {

        this.receiverId = receiverId;

        this.content = content;

        this.title = title;
    }


    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}