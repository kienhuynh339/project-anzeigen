/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;

import sp.backend.location.GeodbLocationDto;

import java.time.OffsetDateTime;

public class ItemWithDistanceDto {
    private Long id;
    private String title;
    private String description;
    private int price;
    private GeodbLocationDto location;
    private OffsetDateTime createdAt;
    private Double distance;

    public ItemWithDistanceDto(Long id, String title, String description, int price, GeodbLocationDto location, OffsetDateTime createdAt, Double distance){
        this.id=id;this.title=title;this.description=description;this.price=price;this.location=location;this.createdAt=createdAt;this.distance=distance;
    }

    public Long getId() {
        return id;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Double getDistance() {
        return distance;
    }
}