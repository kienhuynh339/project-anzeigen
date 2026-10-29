/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final GeodbLocationRepository geodbLocationRepository;

    public ItemService(ItemRepository itemRepository, GeodbLocationRepository geodbLocationRepository) {
        this.itemRepository = itemRepository;
        this.geodbLocationRepository = geodbLocationRepository;
    }

    public ItemDto findById(Long itemId, Long loggedInUserId){
        return itemRepository.findItemById(itemId,loggedInUserId);
    }

    public Page<ItemWithDistanceDto> search(
            String searchTerm,
            Short categoryId,
            Integer minPrice,
            Integer maxPrice,
            Integer locationId,
            Integer radiusKm, Long loggedInUserId,
            Pageable pageable) {

        Float latitude = null;
        Float longtitude = null;
        if (locationId != null && radiusKm != null) {
            Optional<GeodbLocation> location = geodbLocationRepository.findById(locationId);
            if (location.isPresent()) {
                latitude = location.get().getBreite();
                longtitude = location.get().getLaenge();
            }
        }
        return itemRepository.search(searchTerm, categoryId, minPrice, maxPrice, locationId, latitude, longtitude, radiusKm,loggedInUserId, pageable);
    }

    public Page<ItemDto> findByUserId(Long userId, Long loggedInUserId, Pageable pageable){
        return itemRepository.findByUserId(userId, loggedInUserId,pageable);
    }

    public Page<ItemDto> findFavoriteItemsByUserId(Long userId, Pageable pageable){
        return itemRepository.findFavoriteItemsByUserId(userId, pageable);
    }

    @Transactional
    public void markItemAsDeletedByItemId(Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(()->new EntityNotFoundException("Item not found: "+itemId));
        item.setStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }

    @Transactional
    public void updateItemStatus(Long itemId, ItemStatus itemStatus){
        Item item = itemRepository.findById(itemId).orElseThrow(()->new EntityNotFoundException("Item not found: "+itemId));
        item.setStatus(itemStatus);
        itemRepository.save(item);
    }


}
