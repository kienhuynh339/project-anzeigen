/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sp.backend.item.category.Category;
import sp.backend.item.category.CategoryRepository;
import sp.backend.item.item_image.ItemImage;
import sp.backend.item.item_image.ItemImageRepository;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final GeodbLocationRepository geodbLocationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ItemImageRepository itemImageRepository;

    public ItemService(ItemRepository itemRepository, GeodbLocationRepository geodbLocationRepository, CategoryRepository categoryRepository, UserRepository userRepository, ItemImageRepository itemImageRepository) {
        this.itemRepository = itemRepository;
        this.geodbLocationRepository = geodbLocationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.itemImageRepository = itemImageRepository;
    }

    public ItemDto findByItemId(Long itemId, Long loggedInUserId){
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

    @Transactional
    public Long createItem(CreateItemRequest createItemRequest){
        Category category = categoryRepository.findById(createItemRequest.getCategoryId()).orElseThrow(()->new EntityNotFoundException("Category not found: "+createItemRequest.getCategoryId()));
        User poster = userRepository.findById(createItemRequest.getPosterId()).orElseThrow(()->new EntityNotFoundException("User not found: "+createItemRequest.getPosterId()));
        GeodbLocation location = geodbLocationRepository.findById(createItemRequest.getLocationId()).orElseThrow(()->new EntityNotFoundException("Location not found: "+createItemRequest.getLocationId()));

        Item item = new Item(poster,createItemRequest.getTitle(),category,createItemRequest.getDescription(),createItemRequest.getPrice(),location,createItemRequest.getPostcode(),createItemRequest.getLocationExtraInfo(),createItemRequest.getStatus());
        itemRepository.save(item);

        if(createItemRequest.getImages()!=null){
            short order = 0;

            for(MultipartFile file : createItemRequest.getImages()){
                if(file == null || file.isEmpty()){
                    continue;
                }
                try{
                    ItemImage itemImage = new ItemImage(file.getBytes(), file.getContentType(), item.getId(),order++);
                    itemImageRepository.save(itemImage);
                }catch (IOException e){
                    throw new RuntimeException("Error saving image",e);
                }

            }
        }
        return item.getId();
    }


}
