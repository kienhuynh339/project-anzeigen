package sp.backend.item.item_favorite;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

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

    @Transactional
    public ItemFavorite createItemFavorite(Long userId, Long itemId){
        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found: "+userId));
        Item item = itemRepository.findById(itemId).orElseThrow(()->new EntityNotFoundException("Item not found: "+itemId));
        ItemFavorite itemFavorite = new ItemFavorite(user,item);
        return itemFavoriteRepository.save(itemFavorite);
    }

    @Transactional
    public void deleteItemFavorite(Long userId, Long itemId){
        ItemFavorite itemFavorite = itemFavoriteRepository.findByUser_IdAndItem_Id(userId, itemId).orElseThrow(()->new EntityNotFoundException("ItemFavorite not found"));
        itemFavoriteRepository.delete(itemFavorite);
    }

    public ItemFavoriteDto findById(Long itemFavoriteId){
        ItemFavorite itemFavorite = itemFavoriteRepository.findById(itemFavoriteId).orElseThrow(()->new EntityNotFoundException("ItemFavorite not found: "+itemFavoriteId));
        ItemFavoriteDto itemFavoriteDto = new ItemFavoriteDto(itemFavorite.getId(),itemFavorite.getUser().getId(),itemFavorite.getItem().getId());
        return itemFavoriteDto;
    }
}
