package pl.grzegorz.neat.model.message;

public class ArchiveMessageDTO {
    private long messageId;
    private String archiveTarget;

    public String getArchiveTarget() {
        return archiveTarget;
    }

    public void setArchiveTarget(String archiveTarget) {
        this.archiveTarget = archiveTarget;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}