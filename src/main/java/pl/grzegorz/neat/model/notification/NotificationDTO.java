package pl.grzegorz.neat.model.notification;

public class NotificationDTO {

    private long notificationId;
    private String type;
    private String message;

    public static NotificationDTO fromEntity(NotificationEntity entity) {


        NotificationDTO dto = new NotificationDTO(entity.getNotificationType().toString(), entity.getMessage(), entity.getId());
        return dto;
    }



    public NotificationDTO(String type, String message, long id) {
        this.notificationId = id;
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }
}