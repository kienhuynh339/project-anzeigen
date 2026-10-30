package sp.backend.item.item_favorite;

public class CreateItemFavoriteRequest {
    private Long itemId;
    private Long userId;

    public Long getItemId() {
        return itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
