package sp.backend.chat;

import sp.backend.item.ItemStatus;
import sp.backend.user.UserPublicDto;

public class ChatDto {
    private Long id;
    private Long itemId;
    private String itemTitle;
    private ItemStatus itemStatus;
    private UserPublicDto poster;
    private UserPublicDto interestedUser;

    public ChatDto(Long id, Long itemId, String itemTitle, ItemStatus itemStatus, UserPublicDto poster, UserPublicDto interestedUser) {
        this.id = id;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemStatus = itemStatus;
        this.poster = poster;
        this.interestedUser = interestedUser;
    }

    public Long getId() {
        return id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public Long getItemId() {
        return itemId;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public UserPublicDto getPoster() {
        return poster;
    }

    public UserPublicDto getInterestedUser() {
        return interestedUser;
    }
}
