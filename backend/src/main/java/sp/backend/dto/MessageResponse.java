/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import sp.backend.chat.message.Message;

import java.time.OffsetDateTime;

public class MessageResponse {
    private Long id;
    private Long senderId;
    private String content;
    private boolean isRead;
    private OffsetDateTime createdAt;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
        this.content = message.getContent();
        this.isRead = message.isRead();
        this.createdAt = message.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
