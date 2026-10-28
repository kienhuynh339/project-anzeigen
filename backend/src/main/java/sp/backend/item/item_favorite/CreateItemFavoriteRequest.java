package sp.backend.item.item_favorite;

public class CreateItemFavoriteRequest {
    private Long itemId;
    private Long userId;

    public CreateItemFavoriteRequest(Long itemId, Long userId) {
        this.itemId = itemId;
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getUserId() {
        return userId;
    }
}
