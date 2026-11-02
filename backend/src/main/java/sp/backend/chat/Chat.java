/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat;

import jakarta.persistence.*;
import sp.backend.item.Item;
import sp.backend.user.User;

import java.time.OffsetDateTime;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private User poster;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "interested_user_id")
    private User interestedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_deleted_by_user_id")
    private User firstDeletedByUser;

    protected Chat() {
    }

    public Chat(Item item, User poster, User interestedUser, User firstDeletedByUser) {
        this.item = item;
        this.poster = poster;
        this.interestedUser = interestedUser;
        this.firstDeletedByUser = firstDeletedByUser;
    }

    public boolean isParticipant(User user) {
        if (user == null) return false;
        return user.equals(poster) || user.equals(interestedUser);
    }

    public User otherParticipant(User user) {
        if (user == null) return null;
        return user.equals(poster) ? poster : interestedUser;
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public User getPoster() {
        return poster;
    }

    public User getInterestedUser() {
        return interestedUser;
    }

    public User getFirstDeletedByUser() {
        return firstDeletedByUser;
    }

    public void setFirstDeletedByUser(User firstDeletedByUser) {
        this.firstDeletedByUser = firstDeletedByUser;
    }
}

