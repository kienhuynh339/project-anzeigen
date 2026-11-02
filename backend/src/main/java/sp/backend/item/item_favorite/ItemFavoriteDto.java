package sp.backend.item.item_favorite;

public class ItemFavoriteDto {
    private Long itemId;

    public ItemFavoriteDto(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }
}
