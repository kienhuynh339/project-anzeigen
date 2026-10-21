package sp.backend.chat.message;

import jakarta.persistence.*;
import sp.backend.chat.Chat;
import sp.backend.user.User;

import java.time.OffsetDateTime;

@Entity
@Table(name="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="chat_id")
    private Chat chat;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="sender_id")
    private User sender;

    @Column(name="content", columnDefinition = "TEXT")
    private String content;

    @Column(name="is_read")
    private boolean isRead;

    @Column(name="created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Message() {}

    public Message(Chat chat, User sender, String content, boolean isRead) {
        this.chat = chat;
        this.sender = sender;
        this.content = content;
        this.isRead = isRead;
    }

    public Long getId() {
        return id;
    }

    public Chat getChat() {
        return chat;
    }

    public User getSender() {
        return sender;
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

