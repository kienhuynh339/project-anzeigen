package sp.backend.item;

import sp.backend.location.GeodbLocationDto;

import java.time.OffsetDateTime;

public class ItemWithDistanceDto {
    private Long id;
    private String title;
    private String description;
    private int price;
    private GeodbLocationDto location;
    private double distance;
    private OffsetDateTime createdAt;
    public ItemWithDistanceDto(ItemWithDistance itemWithDistance){
        this.id=itemWithDistance.getItem().getId();
        this.title=itemWithDistance.getItem().getTitle();
        this.description=itemWithDistance.getItem().getDescription();
        this.price=itemWithDistance.getItem().getPrice();
        this.location=new GeodbLocationDto(itemWithDistance.getItem().getLocation(),"");
        this.createdAt = itemWithDistance.getItem().getCreatedAt();
        this.distance = itemWithDistance.getDistance();
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

    public double getDistance() {
        return distance;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
