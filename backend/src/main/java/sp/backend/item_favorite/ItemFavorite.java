/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_favorite;

import jakarta.persistence.*;
import sp.backend.item.Item;
import sp.backend.user.User;

@Entity
@Table(name = "item_favorites")
public class ItemFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    protected ItemFavorite() {
    }

    ;

    public ItemFavorite(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }
}