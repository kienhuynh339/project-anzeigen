/**
 * @author Trung Kien Huynh
 */
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

    @Column(name="plz")
    private String postcode;

    @Column(name="location_extra_info")
    private String locationExtraInfo;

    @Enumerated
    private ItemStatus status;

    @Column(name="created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    protected Item(){}

    public Item(User poster, String title, Category category,
                String description, Integer price,
                GeodbLocation location, String postcode, String locationExtraInfo, ItemStatus status) {
        this.poster = poster;
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.location = location;
        this.postcode = postcode;
        this.locationExtraInfo=locationExtraInfo;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public GeodbLocation getLocation() {
        return location;
    }

    public void setLocationId(GeodbLocation location) {
        this.location = location;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String plz) {
        this.postcode = plz;
    }

    public String getLocationExtraInfo() {
        return locationExtraInfo;
    }

    public void setLocationExtraInfo(String locationExtraInfo) {
        this.locationExtraInfo = locationExtraInfo;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}




