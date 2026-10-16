package sp.backend.item;

import jakarta.persistence.*;
import sp.backend.item.category.Category;
import sp.backend.location.GeodbLocation;
import sp.backend.user.User;

import java.time.OffsetDateTime;

@Entity
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="poster_id")
    private User poster;

    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    private String description;

    private int price = 0;

    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name="location_id")
    private GeodbLocation location;

    @Enumerated(EnumType.ORDINAL)
    private ItemStatus status;

    @Column(name="created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    protected Item(){}
}




