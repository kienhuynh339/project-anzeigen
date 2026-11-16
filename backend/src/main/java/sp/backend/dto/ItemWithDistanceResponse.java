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
    private Integer price;
    private GeodbLocationResponse location;
    private OffsetDateTime createdAt;
    private Double distance;
    private Boolean isFavorite;

    public ItemWithDistanceResponse(Long id, Long posterId, String title, String description, Integer price, GeodbLocationResponse location, OffsetDateTime createdAt, Double distance, Boolean isFavorite) {
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

    public ItemWithDistanceResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPosterId() {
        return posterId;
    }

    public void setPosterId(Long posterId) {
        this.posterId = posterId;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public GeodbLocationResponse getLocation() {
        return location;
    }

    public void setLocation(GeodbLocationResponse location) {
        this.location = location;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}