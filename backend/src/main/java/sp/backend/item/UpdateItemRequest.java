package sp.backend.item;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UpdateItemRequest {
    private String title;
    private Short categoryId;
    private String description;
    private Integer locationId;
    private String postcode;
    private String locationExtraInfo;
    private Integer price;
    private ItemStatus status;
    private List<MultipartFile> images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Short categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLocationExtraInfo() {
        return locationExtraInfo;
    }

    public void setLocationExtraInfo(String locationExtraInfo) {
        this.locationExtraInfo = locationExtraInfo;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
