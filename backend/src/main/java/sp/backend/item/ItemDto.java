/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;

import sp.backend.location.GeodbLocationDto;
import sp.backend.user.UserPublicDto;

import java.time.OffsetDateTime;

public class ItemDto {
    private Long id;
    private Short categoryId;
    private UserPublicDto poster;
    private String title;
    private String description;
    private int price;
    private GeodbLocationDto location;
    private ItemStatus status;
    private OffsetDateTime createdAt;
    private Boolean isFavorite;

    public ItemDto(Item item, Boolean isFavorite){
        this.id=item.getId();
        this.categoryId=item.getCategory().getId();
        this.poster =new UserPublicDto(item.getPoster());
        this.title= item.getTitle();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.location= new GeodbLocationDto(item.getLocation().getId(), item.getLocation().getName(),item.getPostcode(),item.getLocationExtraInfo());
        this.status = item.getStatus();
        this.createdAt = item.getCreatedAt();
        this.isFavorite=isFavorite;
    }

    public ItemDto(Long id,Short categoryId, UserPublicDto poster, String title, String description, int price, GeodbLocationDto location, ItemStatus status, OffsetDateTime createdAt, Boolean isFavorite) {
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

    public Short getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Short categoryId) {
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public UserPublicDto getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public GeodbLocationDto getLocation() {
        return location;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }
}
