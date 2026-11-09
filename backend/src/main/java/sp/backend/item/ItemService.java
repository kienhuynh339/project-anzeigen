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
import sp.backend.dto.ItemCreateRequest;
import sp.backend.dto.ItemResponse;
import sp.backend.dto.ItemWithDistanceResponse;
import sp.backend.dto.ItemUpdateRequest;
import sp.backend.exception.DeletedItemModificationNotAllowedException;
import sp.backend.exception.ImageSavingException;
import sp.backend.exception.ImageSizeExceededException;
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

import sp.backend.exception.ExceptionMessage;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Action;
import sp.backend.websocket.DataChangePublisher.Entity;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final GeodbLocationRepository geodbLocationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ItemImageRepository itemImageRepository;
    private final DataChangePublisher dataChangePublisher;

    private static final long MAX_IMAGES_SIZE = 50L * 1024 * 1024;

    public ItemService(ItemRepository itemRepository, GeodbLocationRepository geodbLocationRepository, CategoryRepository categoryRepository, UserRepository userRepository, ItemImageRepository itemImageRepository, DataChangePublisher dataChangePublisher) {
        this.itemRepository = itemRepository;
        this.geodbLocationRepository = geodbLocationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.itemImageRepository = itemImageRepository;
        this.dataChangePublisher = dataChangePublisher;
    }

    public ItemResponse findByItemId(Long itemId, Authentication authentication) {
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);
        return itemRepository.findItemById(itemId, loggedInUserId);
    }

    public ItemResponse findByItemIdToEdit(Long itemId, Authentication authentication) {
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);

        ItemResponse item = itemRepository.findItemById(itemId, loggedInUserId);

        if (!item.getPoster().getId().equals(loggedInUserId)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        return itemRepository.findItemById(itemId, loggedInUserId);
    }

    public Page<ItemWithDistanceResponse> search(
            String searchTerm,
            Short categoryId,
            Integer minPrice,
            Integer maxPrice,
            Integer locationId,
            Integer radiusKm,
            Authentication authentication,
            Pageable pageable) {

        Float latitude = null;
        Float longtitude = null;
        if (locationId != null && radiusKm != null) {
            Optional<GeodbLocation> location = geodbLocationRepository.findById(locationId);
            if (location.isPresent()) {
                latitude = location.get().getLatitude();
                longtitude = location.get().getLongitude();
            }
        }
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);
        return itemRepository.search(searchTerm, categoryId, minPrice, maxPrice, locationId, latitude, longtitude, radiusKm, loggedInUserId, pageable);
    }

    public Page<ItemResponse> findByUserId(Long userId, Authentication authentication, Pageable pageable) {
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);
        return itemRepository.findByUserId(userId, loggedInUserId, pageable);
    }

    public Page<ItemResponse> findFavoriteItems(Authentication authentication, Pageable pageable) {
        Long userId = getLoggedInUserId(authentication, userRepository, true);
        return itemRepository.findFavoriteItemsByUserId(userId, pageable);
    }

    @Transactional
    public void markItemAsDeletedByItemId(Long itemId, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        if (!item.getPoster().equals(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        item.setStatus(ItemStatus.DELETED);
        itemRepository.save(item);

        dataChangePublisher.publish(Entity.ITEM, Action.DELETE, item.getId(), null);
    }

    @Transactional
    public void updateItemStatus(Long itemId, ItemStatus itemStatus, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        validateItemStatus(EnumSet.of(ItemStatus.ACTIVE, ItemStatus.RESERVED, ItemStatus.DELETED), itemStatus);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        if (!item.getPoster().equals(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        item.setStatus(itemStatus);
        itemRepository.save(item);

        dataChangePublisher.publish(Entity.ITEM, Action.UPDATE, item.getId(), null);
    }

    @Transactional
    public Long createItem(ItemCreateRequest request, Authentication authentication) {
        User poster = getLoggedInUser(authentication, userRepository, true);

        validateItemStatus(EnumSet.of(ItemStatus.ACTIVE, ItemStatus.DRAFT), request.getStatus());

        validateImages(request.getImages());

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND));

        GeodbLocation location = geodbLocationRepository.findById(request.getLocationId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.LOCATION_NOT_FOUND));

        Item item = new Item(poster, request.getTitle(), category, request.getDescription(), request.getPrice(), location, request.getPostcode(), request.getLocationExtraInfo(), request.getStatus());
        itemRepository.save(item);

        saveImages(request.getImages(), item.getId());

        dataChangePublisher.publish(Entity.ITEM, Action.CREATE, item.getId(), null);

        return item.getId();
    }

    @Transactional
    public boolean updateItem(Long itemId, ItemUpdateRequest request, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        validateImages(request.getImages());

        // Gelöschtes Item darf nicht modifiziert werden
        if (item.getStatus() == ItemStatus.DELETED) {
            throw new DeletedItemModificationNotAllowedException(ExceptionMessage.DELETED_ITEM_EDITING_NOT_ALLOWED);
        }

        // (Gilt für "Anzeige bearbeiten") Status einer entworfenen Anzeige darf nur in ACTIVE oder DRAFT geändert werden.
        if (item.getStatus() == ItemStatus.DRAFT) {
            validateItemStatus(EnumSet.of(ItemStatus.ACTIVE, ItemStatus.DRAFT), request.getStatus());
        }

        // (Gilt für "Anzeige bearbeiten") Status einer veröffentlichten Anzeige muss ACTIVE bleiben.
        if (item.getStatus() == ItemStatus.ACTIVE) {
            validateItemStatus(EnumSet.of(ItemStatus.ACTIVE), request.getStatus());
        }

        // Ist der eingeloggte User wirklich der Poster der Anzeige
        if (!item.getPoster().getAccountName().equals(loggedInUser.getAccountName())) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND));

        GeodbLocation location = geodbLocationRepository.findById(request.getLocationId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.LOCATION_NOT_FOUND));

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
        saveImages(request.getImages(), item.getId());

        dataChangePublisher.publish(Entity.ITEM, Action.UPDATE, item.getId(), null);

        return true;
    }

    private void validateImages(List<MultipartFile> images) {
        if (images != null) {
            // Gesamtgröße der Bilder
            long totalSize = images.stream()
                    .filter((file) -> file != null && !file.isEmpty())
                    .mapToLong(MultipartFile::getSize)
                    .sum();

            if (totalSize > MAX_IMAGES_SIZE) {
                throw new ImageSizeExceededException(ExceptionMessage.IMAGES_TOTAL_SIZE);
            }
        }
    }

    private void saveImages(List<MultipartFile> images, Long itemId) {
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
                    throw new ImageSavingException(ExceptionMessage.ERROR_SAVING_IMAGE, e);
                }
            }
        }
    }

    private void validateItemStatus(EnumSet<ItemStatus> enumSet, ItemStatus itemStatus) {
        if (!enumSet.contains(itemStatus)) {
            throw new IllegalArgumentException(ExceptionMessage.STATUS_NOT_VALID);
        }
    }
}
