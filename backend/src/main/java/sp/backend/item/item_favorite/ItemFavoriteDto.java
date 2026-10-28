package sp.backend.item.item_favorite;

public class ItemFavoriteDto {
    private Long id;
    private Long userId;
    private Long itemId;

    public ItemFavoriteDto(Long id, Long userId, Long itemId) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getItemId() {
        return itemId;
    }
}
