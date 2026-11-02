/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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

import static sp.backend.utils.Utils.getLoggedInUser;
import static sp.backend.utils.Utils.getLoggedInUserId;
import static sp.backend.utils.Utils.getLoggedInAccountName;

import sp.backend.utils.Utils.ExceptionMessage;

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

    public ItemDto findByItemId(Long itemId, Authentication authentication) {
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);
        return itemRepository.findItemById(itemId, loggedInUserId);
    }

    public Page<ItemWithDistanceDto> search(
            String searchTerm,
            Short categoryId,
            Integer minPrice,
            Integer maxPrice,
            Integer locationId,
            Integer radiusKm, Authentication authentication,
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
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);
        return itemRepository.search(searchTerm, categoryId, minPrice, maxPrice, locationId, latitude, longtitude, radiusKm, loggedInUserId, pageable);
    }

    public Page<ItemDto> findByUserId(Long userId, Authentication authentication, Pageable pageable) {
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);
        return itemRepository.findByUserId(userId, loggedInUserId, pageable);
    }

    public Page<ItemDto> findFavoriteItems(Authentication authentication, Pageable pageable) {
        Long userId = getLoggedInUserId(authentication, userRepository, true);
        return itemRepository.findFavoriteItemsByUserId(userId, pageable);
    }

    @Transactional
    public void markItemAsDeletedByItemId(Long itemId, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND.getMessage()));

        if (!item.getPoster().equals(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED.getMessage());
        }

        item.setStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }

    @Transactional
    public void updateItemStatus(Long itemId, ItemStatus itemStatus, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND.getMessage()));

        if (!item.getPoster().equals(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED.getMessage());
        }

        item.setStatus(itemStatus);
        itemRepository.save(item);
    }

    @Transactional
    public Long createItem(CreateItemRequest request, Authentication authentication) {
        User poster = getLoggedInUser(authentication, userRepository, true);

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND.getMessage()));

        GeodbLocation location = geodbLocationRepository.findById(request.getLocationId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.LOCATION_NOT_FOUND.getMessage()));

        Item item = new Item(poster, request.getTitle(), category, request.getDescription(), request.getPrice(), location, request.getPostcode(), request.getLocationExtraInfo(), request.getStatus());
        itemRepository.save(item);

        saveImage(request.getImages(), item.getId());

        return item.getId();
    }

    @Transactional
    public boolean updateItem(Long itemId, UpdateItemRequest request, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND.getMessage()));

        // Gelöschte Item darf nicht modifiziert werden
        if (item.getStatus() == ItemStatus.DELETED) {
            throw new IllegalStateException(ExceptionMessage.DELETED_ITEM_EDITING_NOT_ALLOWED.getMessage());
        }

        // Ist der eingeloggte User wirklich der Poster der Anzeige
        if (!item.getPoster().getAccountName().equals(loggedInUser.getAccountName())) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED.getMessage());
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND.getMessage()));

        GeodbLocation location = geodbLocationRepository.findById(request.getLocationId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.LOCATION_NOT_FOUND.getMessage()));

        // Lösche alle Bilder der Anzeige in der DB
        itemImageRepository.deleteAllByItemId(itemId);

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
        saveImage(request.getImages(), item.getId());

        return true;
    }

    private void saveImage(List<MultipartFile> images, Long itemId) {
        if (images != null) {
            short order = 0;
            for (MultipartFile file : images) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                try {
                    ItemImage itemImage = new ItemImage(file.getBytes(), file.getContentType(), itemId, order++);
                    itemImageRepository.save(itemImage);
                } catch (IOException e) {
                    throw new RuntimeException(ExceptionMessage.ERROR_SAVING_IMAGE.getMessage(), e);
                }
            }
        }
    }
}
