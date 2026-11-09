package sp.backend.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;
import sp.backend.item.ItemStatus;
import sp.backend.exception.ExceptionMessage;

import java.util.List;

public class ItemUpdateRequest {
    @NotBlank(message = ExceptionMessage.ITEM_TITEL_NOT_BLANK)
    @Size(min = 5, max = 100, message = ExceptionMessage.ITEM_TITEL_SIZE)
    private String title;

    @NotNull(message = ExceptionMessage.CATEGORY_ID_NOT_NULL)
    @Positive(message = ExceptionMessage.CATEGORY_ID_POSITIVE)
    private Short categoryId;

    @Size(max = 4000, message = ExceptionMessage.DESCRIPTION_SIZE)
    private String description;

    @NotNull(message = ExceptionMessage.LOCATION_ID_NOT_NULL)
    @Positive(message = ExceptionMessage.LOCATION_ID_POSITIVE)
    private Integer locationId;

    @NotBlank(message = ExceptionMessage.POSTCODE_NOT_BLANK)
    @Pattern(regexp = "^[0-9]{5}$", message = ExceptionMessage.POSTCODE_PATTERN)
    private String postcode;

    @Size(max = 50, message = ExceptionMessage.LOCATION_EXTRA_INFO_SIZE)
    private String locationExtraInfo;

    @NotNull(message = ExceptionMessage.PRICE_NOT_NULL)
    @PositiveOrZero(message = ExceptionMessage.PRICE_POSITIVE_OR_ZERO)
    private Integer price;

    @NotNull(message = ExceptionMessage.STATUS_NOT_NULL)
    private ItemStatus status;

    @Size(max = 10, message = ExceptionMessage.IMAGES_AMOUNT)
    private List<MultipartFile> images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
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
