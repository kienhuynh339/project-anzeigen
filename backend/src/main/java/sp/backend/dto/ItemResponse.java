/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import sp.backend.item.Item;
import sp.backend.item.ItemStatus;

import java.time.OffsetDateTime;

public class ItemResponse {
    private Long id;
    private Short categoryId;
    private UserPublicResponse poster;
    private String title;
    private String description;
    private int price;
    private GeodbLocationResponse location;
    private ItemStatus status;
    private OffsetDateTime createdAt;
    private Boolean isFavorite;

    public ItemResponse(Item item, Boolean isFavorite) {
        this.id = item.getId();
        this.categoryId = item.getCategory().getId();
        this.poster = new UserPublicResponse(item.getPoster());
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.location = new GeodbLocationResponse(item.getLocation().getId(), item.getLocation().getName(), item.getPostcode(), item.getLocationExtraInfo());
        this.status = item.getStatus();
        this.createdAt = item.getCreatedAt();
        this.isFavorite = isFavorite;
    }

    public ItemResponse(Long id, Short categoryId, UserPublicResponse poster, String title, String description, int price, GeodbLocationResponse location, ItemStatus status, OffsetDateTime createdAt, Boolean isFavorite) {
        this.id = id;
        this.categoryId = categoryId;
        this.poster = poster;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
        this.isFavorite = isFavorite;
    }

    public ItemResponse() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Short categoryId) {
        this.categoryId = categoryId;
    }

    public UserPublicResponse getPoster() {
        return poster;
    }

    public void setPoster(UserPublicResponse poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public GeodbLocationResponse getLocation() {
        return location;
    }

    public void setLocation(GeodbLocationResponse location) {
        this.location = location;
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

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
