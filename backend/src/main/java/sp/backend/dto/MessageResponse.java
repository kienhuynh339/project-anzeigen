/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import sp.backend.chat_message.Message;

import java.time.OffsetDateTime;

public class MessageResponse {
    private Long id;
    private Long senderId;
    private String content;
    private Boolean isRead;
    private OffsetDateTime createdAt;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
        this.content = message.getContent();
        this.isRead = message.getRead();
        this.createdAt = message.getCreatedAt();
    }

    public MessageResponse() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
