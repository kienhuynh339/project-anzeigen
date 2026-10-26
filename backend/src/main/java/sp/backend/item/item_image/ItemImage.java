/**
 * @author Trung Kien Huynh
 */
package sp.backend.item.item_image;

import jakarta.persistence.*;

@Entity
@Table(name="item_images")
public class ItemImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] image;

    @Column(name="content_type")
    private String contentType;

    @Column(name="item_id")
    private Long itemId;

    @Column(name="display_order")
    private Short displayOrder;

    protected ItemImage(){};

    public ItemImage(byte[] image, Long itemId, Short displayOrder) {
        this.image = image;
        this.itemId = itemId;
        this.displayOrder = displayOrder;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType(){return contentType;}

    public void setContentType(String contentType){this.contentType = contentType;}

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Short getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Short displayOrder) {
        this.displayOrder = displayOrder;
    }
}

