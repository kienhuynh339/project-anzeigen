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
    public Long createItem(CreateItemRequest request){
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()->new EntityNotFoundException("Category not found: "+request.getCategoryId()));
        User poster = userRepository.findById(request.getPosterId()).orElseThrow(()->new EntityNotFoundException("User not found: "+request.getPosterId()));
        GeodbLocation location = geodbLocationRepository.findById(request.getLocationId()).orElseThrow(()->new EntityNotFoundException("Location not found: "+request.getLocationId()));

        Item item = new Item(poster,request.getTitle(),category,request.getDescription(),request.getPrice(),location,request.getPostcode(),request.getLocationExtraInfo(),request.getStatus());
        itemRepository.save(item);

        if(request.getImages()!=null){
            short order = 0;

            for(MultipartFile file : request.getImages()){
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

    @Transactional
    public boolean updateItem(Long itemId,UpdateItemRequest request, Long loggedInUserId){
        Item item = itemRepository.findById(itemId).orElseThrow(()->new EntityNotFoundException("Item not found: "+itemId));
        // Gelöschte Item darf nicht modifiziert werden
        if(item.getStatus()==ItemStatus.DELETED){
            throw new IllegalStateException("Deleted item cannot be modified: "+itemId);
        }
        // Ist der User wirklich der Poster der Anzeige
        if(!loggedInUserId.equals(item.getPoster().getId())){
            throw new EntityNotFoundException("Item not found: "+itemId);
        }
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()->new EntityNotFoundException("Category not found: "+request.getCategoryId()));
        GeodbLocation location = geodbLocationRepository.findById(request.getLocationId()).orElseThrow(()->new EntityNotFoundException("Location not found: "+ request.getLocationId()));
        // Lösche alle Bilder der Anzeige in der DB
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByDisplayOrderAsc(itemId);
        itemImages.forEach(itemImageRepository::delete);

        // Item aktualisieren
        item.setTitle(request.getTitle());
        item.setCategory(category);
        item.setDescription(request.getDescription());
        item.setLocation(location);
        item.setPostcode(request.getPostcode());
        item.setLocationExtraInfo(request.getLocationExtraInfo());
        item.setPrice(request.getPrice());
        item.setStatus(request.getStatus());

        itemRepository.save(item);

        // Bilder speichern
        if(request.getImages() != null){
            short order = 0;
            for(MultipartFile file : request.getImages()){
                if(file == null || file.isEmpty()){
                    continue;
                }
                try{
                    ItemImage itemImage = new ItemImage(file.getBytes(), file.getContentType(), item.getId(),order++);
                    itemImageRepository.save(itemImage);
                }catch(IOException e){
                    throw new RuntimeException("Error saving images",e);
                }
            }
        }
        return true;
    }
}
