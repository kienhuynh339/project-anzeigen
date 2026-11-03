package sp.backend.dto;

public class ItemFavoriteResponse {
    private Long itemId;

    public ItemFavoriteResponse(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }
}
