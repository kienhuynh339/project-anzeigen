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
import sp.backend.dto.*;
import sp.backend.exception.DeletedItemModificationNotAllowedException;
import sp.backend.exception.ImageSavingException;
import sp.backend.exception.ImageSizeExceededException;
import sp.backend.item_category.Category;
import sp.backend.item_category.CategoryRepository;
import sp.backend.item_image.ItemImage;
import sp.backend.item_image.ItemImageRepository;
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

    public ItemResponse findById(Long itemId, Authentication authentication) {
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository);

        return itemRepository.findItemById(itemId, loggedInUserId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));
    }

    public ItemResponse findByIdToEdit(Long itemId, Authentication authentication) {
        Long loggedInUserId = getLoggedInUserId(authentication, userRepository, true);

        ItemResponse item = itemRepository.findItemById(itemId, loggedInUserId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        // Ob der User der Poster ist
        if (!item.getPoster().getId().equals(loggedInUserId)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        return item;
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

        // Breitengrad
        Float latitude = null;
        // Längengrad
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

        User poster = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND));

        return itemRepository.findByUserId(poster.getId(), loggedInUserId, pageable);
    }

    public Page<ItemResponse> findFavoriteItems(Authentication authentication, Pageable pageable) {
        Long userId = getLoggedInUserId(authentication, userRepository, true);
        return itemRepository.findFavoriteItemsByUserId(userId, pageable);
    }

    @Transactional
    public void markItemAsDeleted(Long itemId, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        // Ob der User der Poster ist
        if (!item.getPoster().equals(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        item.setStatus(ItemStatus.DELETED);
        itemRepository.save(item);

        dataChangePublisher.publish(Entity.ITEM, Action.DELETE, item);
    }

    @Transactional
    public void updateItemStatus(Long itemId, ItemStatus itemStatus, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        // Ob der User der Poster ist
        if (!item.getPoster().equals(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        validateItemStatus(EnumSet.of(ItemStatus.ACTIVE, ItemStatus.RESERVED, ItemStatus.DELETED), itemStatus);

        // Gelöschte Anzeige darf nicht modifiziert werden
        if (item.getStatus() == ItemStatus.DELETED) {
            throw new DeletedItemModificationNotAllowedException(ExceptionMessage.DELETED_ITEM_EDITING_NOT_ALLOWED);
        }

        item.setStatus(itemStatus);
        itemRepository.save(item);

        dataChangePublisher.publish(Entity.ITEM, Action.UPDATE, item);
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

        saveImages(request.getImages(), item.getId());

        itemRepository.save(item);

//        dataChangePublisher.publish(Entity.ITEM, Action.CREATE, item);

        return item.getId();
    }

    @Transactional
    public boolean updateItem(Long itemId, ItemUpdateRequest request, Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        // Ob der User der Poster ist
        if (!item.getPoster().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }

        validateImages(request.getImages());

        // Gelöschte Anzeige darf nicht modifiziert werden
        if (item.getStatus() == ItemStatus.DELETED) {
            throw new DeletedItemModificationNotAllowedException(ExceptionMessage.DELETED_ITEM_EDITING_NOT_ALLOWED);
        }

        // (Gilt für "Entwurf fortsetzen") Status einer entworfenen Anzeige darf nur in ACTIVE oder DRAFT geändert werden.
        if (item.getStatus() == ItemStatus.DRAFT) {
            validateItemStatus(EnumSet.of(ItemStatus.ACTIVE, ItemStatus.DRAFT), request.getStatus());
        }

        // (Gilt für "Anzeige bearbeiten") Status einer veröffentlichten Anzeige muss ACTIVE bleiben.
        if (item.getStatus() == ItemStatus.ACTIVE) {
            validateItemStatus(EnumSet.of(ItemStatus.ACTIVE), request.getStatus());
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND));

        GeodbLocation location = geodbLocationRepository.findById(request.getLocationId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.LOCATION_NOT_FOUND));

        // Lösche alle Bilder der Anzeige in der DB
        itemImageRepository.deleteAllByItemId(itemId);

        // Bilder speichern
        saveImages(request.getImages(), item.getId());

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

        dataChangePublisher.publish(Entity.ITEM, Action.UPDATE, item);

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
