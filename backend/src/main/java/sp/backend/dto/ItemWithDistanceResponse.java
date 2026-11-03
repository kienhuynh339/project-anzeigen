/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import java.time.OffsetDateTime;

public class ItemWithDistanceResponse {
    private Long id;
    private Long posterId;
    private String title;
    private String description;
    private int price;
    private GeodbLocationResponse location;
    private OffsetDateTime createdAt;
    private Double distance;
    private Boolean isFavorite;

    public ItemWithDistanceResponse(Long id, Long posterId, String title, String description, int price, GeodbLocationResponse location, OffsetDateTime createdAt, Double distance, Boolean isFavorite) {
        this.id = id;
        this.posterId = posterId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.createdAt = createdAt;
        this.distance = distance;
        this.isFavorite = isFavorite;
    }

    public Long getId() {
        return id;
    }

    public Long getPosterId() {
        return posterId;
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

    public GeodbLocationResponse getLocation() {
        return location;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Double getDistance() {
        return distance;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }
}