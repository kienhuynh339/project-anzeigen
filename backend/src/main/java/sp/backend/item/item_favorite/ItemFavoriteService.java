package sp.backend.item.item_favorite;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sp.backend.dto.ItemFavoriteResponse;
import sp.backend.exception.CannotFavorDeletedItemException;
import sp.backend.exception.CannotFavorOwnItemException;
import sp.backend.exception.FavoriteItemAlreadyExistsException;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
import sp.backend.item.ItemStatus;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.exception.ExceptionMessage;

import java.util.Optional;

import static sp.backend.utils.Utils.getLoggedInUser;
import static sp.backend.utils.Utils.getLoggedInUserId;

@Service
public class ItemFavoriteService {
    private final ItemFavoriteRepository itemFavoriteRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemFavoriteService(ItemFavoriteRepository itemFavoriteRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemFavoriteRepository = itemFavoriteRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public ItemFavoriteResponse findById(Long itemFavoriteId, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        ItemFavorite itemFavorite = itemFavoriteRepository.findById(itemFavoriteId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.FAVORITE_ITEM_NOT_FOUND));

        // Ob der Favorite dem angemeldeten User gehört
        if (!itemFavorite.getUser().equals(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        return new ItemFavoriteResponse(itemFavorite.getItem().getId());
    }

    @Transactional
    public ItemFavorite createItemFavorite(Long itemId, Authentication authentication) {
        User user = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        // Poster darf seine eigenen Anzeigen nicht zur Merkliste hinzufügen
        if (item.getPoster().equals(user)) {
            throw new CannotFavorOwnItemException(ExceptionMessage.POSTER_CANNOT_FAVOR_OWN_ITEM);
        }

        // Gelöschte Anzeige darf nicht zur Merkliste hinzugefügt werden
        if (item.getStatus() == ItemStatus.DELETED) {
            throw new CannotFavorDeletedItemException(ExceptionMessage.CANNOT_FAVOR_DELETED_ITEM);
        }

        Optional<ItemFavorite> itemFavoriteOptional = itemFavoriteRepository.findByUser_IdAndItem_Id(user.getId(), item.getId());
        // Anzeige existiert bereits in der Merkliste
        if (itemFavoriteOptional.isPresent()) {
            throw new FavoriteItemAlreadyExistsException(ExceptionMessage.FAVORITE_ITEM_ALREADY_EXISTS);
        }

        ItemFavorite itemFavorite = new ItemFavorite(user, item);
        return itemFavoriteRepository.save(itemFavorite);
    }

    @Transactional
    public void deleteItemFavorite(Long itemId, Authentication authentication) {
        Long userId = getLoggedInUserId(authentication, userRepository, true);

        ItemFavorite itemFavorite = itemFavoriteRepository.findByUser_IdAndItem_Id(userId, itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.FAVORITE_ITEM_NOT_FOUND));

        itemFavoriteRepository.delete(itemFavorite);
    }
}
