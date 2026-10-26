/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;

import sp.backend.location.GeodbLocationDto;
import sp.backend.user.UserPublicDto;

import java.time.OffsetDateTime;

public class ItemDto {
    private Long id;
    private UserPublicDto poster;
    private String title;
    private String description;
    private int price;
    private GeodbLocationDto location;
    private ItemStatus status;
    private OffsetDateTime createdAt;

    public ItemDto(Long id,
                   UserPublicDto poster,
                   String title,
                   String description,
                   int price,
                   GeodbLocationDto location,
                   ItemStatus status,
                   OffsetDateTime createdAt){
        this.id=id;this.poster = poster;this.title=title;this.description=description;this.price=price;this.location=location;this.status=status;this.createdAt=createdAt;
    }
    public ItemDto(Item item){
        this.id=item.getId();
        this.poster =new UserPublicDto(item.getPoster());
        this.title= item.getTitle();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.location= new GeodbLocationDto(item.getLocation().getId(), item.getLocation().getName(),item.getPlz(),item.getLocationExtraInfo());
        this.status = item.getStatus();
        this.createdAt = item.getCreatedAt();
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
}
