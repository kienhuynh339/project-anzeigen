package sp.backend.item;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSources;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import sp.backend.dto.*;
import sp.backend.exception.DeletedItemModificationNotAllowedException;
import sp.backend.exception.ExceptionMessage;
import sp.backend.exception.ImageSavingException;
import sp.backend.exception.ImageSizeExceededException;
import sp.backend.item_category.Category;
import sp.backend.item_category.CategoryRepository;
import sp.backend.item_image.ItemImageRepository;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Action;
import sp.backend.websocket.DataChangePublisher.Entity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @MockitoBean
    private ItemRepository itemRepository;

    @MockitoBean
    private GeodbLocationRepository geodbLocationRepository;

    @MockitoBean
    private CategoryRepository categoryRepository;

    @MockitoBean
    private ItemImageRepository itemImageRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private DataChangePublisher dataChangePublisher;

    private Authentication authentication;
    private User loggedInUser;
    private User otherUser;
    private Item item;
    private final Pageable pageable = PageRequest.of(0, 10);


    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken("loggedInUser", "123456");

        loggedInUser = mock(User.class);
        otherUser = mock(User.class);
        item = mock(Item.class);

        when(userRepository.findByAccountName("loggedInUser")).thenReturn(Optional.of(loggedInUser));

        when(item.getId()).thenReturn(1L);

        when(loggedInUser.getId()).thenReturn(1L);
        when(loggedInUser.getAccountName()).thenReturn("loggedInUser");

        when(otherUser.getId()).thenReturn(2L);
        when(otherUser.getAccountName()).thenReturn("otherUser");

    }

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Test
    void findById_shouldThrowWhenItemDoesNotExist() {
        when(itemRepository.findItemById(1L, loggedInUser.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemService.findById(1L, authentication));
        assertEquals(ExceptionMessage.ITEM_NOT_FOUND, exception.getMessage());
    }

    @Test
    void findById_shouldReturnItemResponse() {
        ItemResponse itemResponse = mock(ItemResponse.class);

        when(itemRepository.findItemById(1L, loggedInUser.getId())).thenReturn(Optional.of(itemResponse));

        ItemResponse result = itemService.findById(1L, authentication);
        assertNotNull(result);
    }

    // ---------------------------------------------------------
    // findByIdToEdit()
    // ---------------------------------------------------------

    @Test
    void findByIdToEdit_shouldThrowWhenUserIsNotAuthenticated() {
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> itemService.findByIdToEdit(1L, null));
        assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

        verifyNoInteractions(itemRepository);
    }

    @Test
    void findByIdToEdit_shouldThrowWhenItemDoesNotExist() {
        when(itemRepository.findItemById(1L, loggedInUser.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemService.findByIdToEdit(1L, authentication));
        assertEquals(ExceptionMessage.ITEM_NOT_FOUND, exception.getMessage());

        verify(item, never()).getPoster();
    }

    @Test
    void findByIdToEdit_shouldThrowWhenUserIsNotTheOwner() {
        ItemResponse itemResponse = mock(ItemResponse.class);

        UserPublicResponse poster = new UserPublicResponse(otherUser);

        when(itemResponse.getPoster()).thenReturn(poster);

        when(itemRepository.findItemById(1L, loggedInUser.getId())).thenReturn(Optional.of(itemResponse));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> itemService.findByIdToEdit(1L, authentication));
        assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());
    }

    @Test
    void findByIdToEdit_shouldReturnItemResponse() {
        ItemResponse itemResponse = mock(ItemResponse.class);

        when(itemResponse.getId()).thenReturn(1L);

        UserPublicResponse poster = new UserPublicResponse(loggedInUser);

        when(itemResponse.getPoster()).thenReturn(poster);

        when(itemRepository.findItemById(1L, loggedInUser.getId())).thenReturn(Optional.of(itemResponse));

        ItemResponse result = itemService.findByIdToEdit(1L, authentication);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    // ---------------------------------------------------------
    // search()
    // ---------------------------------------------------------

    @Nested
    class SearchTests {
        Page<ItemWithDistanceResponse> page;
        GeodbLocation location;

        @BeforeEach
        void setUp() {
            ItemWithDistanceResponse itemWithDistanceResponse1 = new ItemWithDistanceResponse();
            itemWithDistanceResponse1.setId(1L);
            ItemWithDistanceResponse itemWithDistanceResponse2 = new ItemWithDistanceResponse();
            itemWithDistanceResponse2.setId(2L);

            page = new PageImpl<>(List.of(
                    itemWithDistanceResponse1,
                    itemWithDistanceResponse2
            ));

            location = mock(GeodbLocation.class);
            when(location.getLatitude()).thenReturn(10F);
            when(location.getLongitude()).thenReturn(20F);

            when(itemRepository.search(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(page);
        }

        @Test
        void search_shouldCallItemRepositorySearchWithoutCoordinatesWhenLocationIdIsNull() {
            Integer locationId = null;
            Integer radiusKm = 10;


            Page<ItemWithDistanceResponse> result = itemService.search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    radiusKm,
                    authentication,
                    pageable);


            assertNotNull(result);
            assertEquals(2, result.getTotalElements());

            verifyNoInteractions(geodbLocationRepository);
            verify(itemRepository).search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    null,
                    null,
                    radiusKm,
                    loggedInUser.getId(),
                    pageable);
        }

        @Test
        void search_shouldCallItemRepositorySearchWithoutCoordinatesWhenRadiusKmIsNull() {
            Integer locationId = 111;
            Integer radiusKm = null;

            Page<ItemWithDistanceResponse> result = itemService.search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    radiusKm,
                    authentication,
                    pageable);


            assertNotNull(result);
            assertEquals(2, result.getTotalElements());

            verifyNoInteractions(geodbLocationRepository);
            verify(itemRepository).search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    null,
                    null,
                    radiusKm,
                    loggedInUser.getId(),
                    pageable);
        }

        @Test
        void search_shouldCallItemRepositorySearchWithoutCoordinatesWhenRadiusKmAndLocationIdAreNotNullAndLocationDoesNotExist() {
            Integer locationId = 111;
            Integer radiusKm = 10;

            when(geodbLocationRepository.findById(locationId)).thenReturn(Optional.empty());

            Page<ItemWithDistanceResponse> result = itemService.search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    radiusKm,
                    authentication,
                    pageable);


            assertNotNull(result);
            assertEquals(2, result.getTotalElements());

            verify(geodbLocationRepository).findById(locationId);
            verify(itemRepository).search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    null,
                    null,
                    radiusKm,
                    loggedInUser.getId(),
                    pageable);
        }

        @Test
        void search_shouldCallItemRepositorySearchWithCoordinatesWhenRadiusKmAndLocationIdAreNotNull() {
            Integer locationId = 111;
            Integer radiusKm = 10;

            when(geodbLocationRepository.findById(locationId)).thenReturn(Optional.of(location));

            Page<ItemWithDistanceResponse> result = itemService.search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    radiusKm,
                    authentication,
                    pageable);


            assertNotNull(result);
            assertEquals(2, result.getTotalElements());

            verify(geodbLocationRepository).findById(locationId);
            verify(itemRepository).search(
                    "",
                    null,
                    null,
                    null,
                    locationId,
                    location.getLatitude(),
                    location.getLongitude(),
                    radiusKm,
                    loggedInUser.getId(),
                    pageable);
        }
    }

    // ---------------------------------------------------------
    // findByUserId()
    // ---------------------------------------------------------

    @Nested
    class FindByUserIdTests {

        @Test
        void findByUserId_shouldThrowWhenUserDoesNotExist() {
            when(userRepository.findById(3L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.findByUserId(3L, authentication, pageable));
            assertEquals(ExceptionMessage.USER_NOT_FOUND, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void findByUserId_shouldReturnItemResponses() {
            when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));

            Page<ItemResponse> page;

            ItemResponse ItemResponse1 = new ItemResponse();
            ItemResponse ItemResponse2 = new ItemResponse();

            page = new PageImpl<>(List.of(
                    ItemResponse1,
                    ItemResponse2
            ));

            when(itemRepository.findByUserId(otherUser.getId(), loggedInUser.getId(), pageable)).thenReturn(page);

            Page<ItemResponse> result = itemService.findByUserId(otherUser.getId(), authentication, pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }

    // ---------------------------------------------------------
    // findFavoriteItems()
    // ---------------------------------------------------------

    @Nested
    class FindFavoriteItemsTests {

        @Test
        void findFavoriteItems_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemService.findFavoriteItems(null, pageable));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void findFavoriteItems_shouldReturnItemResponses() {
            Page<ItemResponse> page;

            ItemResponse ItemResponse1 = new ItemResponse();
            ItemResponse ItemResponse2 = new ItemResponse();

            page = new PageImpl<>(List.of(
                    ItemResponse1,
                    ItemResponse2
            ));

            when(itemRepository.findFavoriteItemsByUserId(loggedInUser.getId(), pageable)).thenReturn(page);

            Page<ItemResponse> result = itemService.findFavoriteItems(authentication, pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }

    // ---------------------------------------------------------
    // markItemAsDeleted()
    // ---------------------------------------------------------

    @Nested
    class MarkItemAsDeletedTests {

        @Test
        void markItemAsDeleted_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemService.markItemAsDeleted(1L, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void markItemAsDeleted_shouldThrowWhenItemDoesNotExist() {
            when(itemRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.markItemAsDeleted(1L, authentication));
            assertEquals(ExceptionMessage.ITEM_NOT_FOUND, exception.getMessage());

            verify(item, never()).getPoster();
        }

        @Test
        void markItemAsDeleted_shouldThrowWhenUserIsNotTheOwner() {
            when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(otherUser);

            AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                    () -> itemService.markItemAsDeleted(1L, authentication));
            assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());

            verify(item, never()).setStatus(ItemStatus.DELETED);
        }

        @Test
        void markItemAsDeleted_shouldMarkItemAsDeleted() {
            when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(loggedInUser);

            itemService.markItemAsDeleted(1L, authentication);

            verify(item).setStatus(ItemStatus.DELETED);
            verify(itemRepository).save(item);
            verify(dataChangePublisher).publish(
                    Entity.ITEM,
                    Action.DELETE,
                    item);
        }
    }

    // ---------------------------------------------------------
    // updateItemStatus()
    // ---------------------------------------------------------

    @Nested
    class UpdateItemStatusTests {
        private final Long itemId = 1L;

        @Test
        void updateItemStatus_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemService.updateItemStatus(itemId, ItemStatus.ACTIVE, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void updateItemStatus_shouldThrowWhenStatusIsDraft() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> itemService.updateItemStatus(itemId, ItemStatus.DRAFT, authentication));
            assertEquals(ExceptionMessage.STATUS_NOT_VALID, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void updateItemStatus_shouldThrowWhenItemDoesNotExist() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.updateItemStatus(itemId, ItemStatus.ACTIVE, authentication));
            assertEquals(ExceptionMessage.ITEM_NOT_FOUND, exception.getMessage());

            verify(item, never()).getPoster();
        }

        @Test
        void updateItemStatus_shouldThrowWhenUserIsNotTheOwner() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(otherUser);

            AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                    () -> itemService.updateItemStatus(itemId, ItemStatus.ACTIVE, authentication));
            assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());

            verify(item, never()).getStatus();
        }

        @Test
        void updateItemStatus_shouldThrowWhenItemIsDeleted() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(loggedInUser);

            when(item.getStatus()).thenReturn(ItemStatus.DELETED);

            DeletedItemModificationNotAllowedException exception = assertThrows(DeletedItemModificationNotAllowedException.class,
                    () -> itemService.updateItemStatus(itemId, ItemStatus.ACTIVE, authentication));
            assertEquals(ExceptionMessage.DELETED_ITEM_EDITING_NOT_ALLOWED, exception.getMessage());

            verify(item, never()).setStatus(ItemStatus.ACTIVE);
        }

        @Test
        void updateItemStatus_shouldUpdateItemStatus() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(loggedInUser);

            when(item.getStatus()).thenReturn(ItemStatus.RESERVED);

            itemService.updateItemStatus(itemId, ItemStatus.ACTIVE, authentication);

            verify(item).setStatus(ItemStatus.ACTIVE);
            verify(itemRepository).save(item);
            verify(dataChangePublisher).publish(Entity.ITEM, Action.UPDATE, item);
        }
    }

    // ---------------------------------------------------------
    // createItem()
    // ---------------------------------------------------------

    @Nested
    class CreateItemTests {
        private ItemCreateRequest request;

        @BeforeEach
        void setUp() {
            request = new ItemCreateRequest();
        }

        @Test
        void createItem_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemService.createItem(request, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(categoryRepository);
        }

        @ParameterizedTest
        @EnumSource(value = ItemStatus.class, names = {"DELETED", "RESERVED"})
        void createItem_shouldThrowWhenRequestItemStatusIsInvalid(ItemStatus status) {
            request.setStatus(status);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> itemService.createItem(request, authentication));
            assertEquals(ExceptionMessage.STATUS_NOT_VALID, exception.getMessage());

            verifyNoInteractions(categoryRepository);
        }

        @Test
        void createItem_shouldThrowWhenRequestItemStatusIsReserved() {
            request.setStatus(ItemStatus.RESERVED);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> itemService.createItem(request, authentication));
            assertEquals(ExceptionMessage.STATUS_NOT_VALID, exception.getMessage());

            verifyNoInteractions(categoryRepository);
        }

        @Test
        void createItem_shouldThrowWhenTotalImageSizeExceedsLimit() {
            MultipartFile image1 = mock(MultipartFile.class);
            MultipartFile image2 = mock(MultipartFile.class);

            when(image1.isEmpty()).thenReturn(false);
            when(image2.isEmpty()).thenReturn(false);

            when(image1.getSize()).thenReturn(30L * 1024 * 1024);
            when(image2.getSize()).thenReturn(21L * 1024 * 1024);

            request.setImages(List.of(image1, image2));
            request.setStatus(ItemStatus.ACTIVE);

            ImageSizeExceededException exception = assertThrows(ImageSizeExceededException.class,
                    () -> itemService.createItem(request, authentication));
            assertEquals(ExceptionMessage.IMAGES_TOTAL_SIZE, exception.getMessage());

            verifyNoInteractions(categoryRepository);
        }

        @Test
        void createItem_shouldThrowWhenCategoryDoesNotExist() {
            MultipartFile image1 = mock(MultipartFile.class);
            MultipartFile image2 = mock(MultipartFile.class);

            when(image1.isEmpty()).thenReturn(false);
            when(image2.isEmpty()).thenReturn(false);

            when(image1.getSize()).thenReturn(30L * 1024 * 1024);
            when(image2.getSize()).thenReturn(19L * 1024 * 1024);

            request.setImages(List.of(image1, image2));
            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 50);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.createItem(request, authentication));
            assertEquals(ExceptionMessage.CATEGORY_NOT_FOUND, exception.getMessage());

            verifyNoInteractions(geodbLocationRepository);
        }

        @Test
        void createItem_shouldThrowWhenLocationDoesNotExist() {
            request.setImages(List.of());
            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 50);
            request.setLocationId(111);

            Category category = mock(Category.class);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

            when(geodbLocationRepository.findById(request.getLocationId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.createItem(request, authentication));
            assertEquals(ExceptionMessage.LOCATION_NOT_FOUND, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void createItem_shouldThrowWhenImageSavingFails() throws IOException {
            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 50);
            request.setLocationId(111);

            MultipartFile image = mock(MultipartFile.class);

            when(image.isEmpty()).thenReturn(false);
//            when(image.getSize()).thenReturn(1024L);
            when(image.getBytes()).thenThrow(new IOException("IOException"));

            request.setImages(List.of(image));

            Category category = mock(Category.class);
            GeodbLocation location = mock(GeodbLocation.class);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

            when(geodbLocationRepository.findById(request.getLocationId())).thenReturn(Optional.of(location));

            ImageSavingException exception = assertThrows(ImageSavingException.class,
                    () -> itemService.createItem(request, authentication));
            assertEquals(ExceptionMessage.ERROR_SAVING_IMAGE, exception.getMessage());
        }

        @Test
        void createItem_shouldCreateItem() {
            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 50);
            request.setLocationId(111);
            request.setImages(List.of());

            Category category = mock(Category.class);
            GeodbLocation location = mock(GeodbLocation.class);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

            when(geodbLocationRepository.findById(request.getLocationId())).thenReturn(Optional.of(location));

            when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
                // invocation.getArgument(0) = das übergebene Item
                Item savedItem = invocation.getArgument(0);
                savedItem.setId(111L);
                return savedItem;
            });

            Long itemId = itemService.createItem(request, authentication);

            assertEquals(111L, itemId);

            ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
            // capture() speichert das übergebene Item in itemCaptor
            verify(itemRepository).save(itemCaptor.capture());

            assertEquals(loggedInUser.getId(), itemCaptor.getValue().getPoster().getId());
        }
    }

    // ---------------------------------------------------------
    // updateItem()
    // ---------------------------------------------------------

    @Nested
    class UpdateItemTests {
        private ItemUpdateRequest request;
        private final Long itemId = 1L;

        @BeforeEach
        void setUp() {
            request = new ItemUpdateRequest();
        }

        @Test
        void updateItem_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemService.updateItem(itemId, request, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void updateItem_shouldThrowWhenItemDoesNotExist() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.ITEM_NOT_FOUND, exception.getMessage());

            verify(item, never()).getStatus();
        }

        @Test
        void updateItem_shouldThrowWhenTotalImageSizeExceedsLimit() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            MultipartFile image1 = mock(MultipartFile.class);
            MultipartFile image2 = mock(MultipartFile.class);

            when(image1.isEmpty()).thenReturn(false);
            when(image2.isEmpty()).thenReturn(false);

            when(image1.getSize()).thenReturn(30L * 1024 * 1024);
            when(image2.getSize()).thenReturn(21L * 1024 * 1024);

            request.setImages(List.of(image1, image2));

            ImageSizeExceededException exception = assertThrows(ImageSizeExceededException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.IMAGES_TOTAL_SIZE, exception.getMessage());

            verify(item, never()).getStatus();
        }

        @Test
        void updateItem_shouldThrowWhenItemIsDeleted() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            request.setImages(List.of());

            when(item.getStatus()).thenReturn(ItemStatus.DELETED);

            DeletedItemModificationNotAllowedException exception = assertThrows(DeletedItemModificationNotAllowedException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.DELETED_ITEM_EDITING_NOT_ALLOWED, exception.getMessage());

            verify(item, never()).getPoster();
        }

        @ParameterizedTest
        @EnumSource(value = ItemStatus.class, names = {"DELETED", "RESERVED"})
        void updateItem_shouldThrowWhenItemStatusIsDraftAndRequestItemStatusIsInvalid(ItemStatus status) {
            request.setStatus(status);
            request.setImages(List.of());

            when(item.getStatus()).thenReturn(ItemStatus.DRAFT);

            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.STATUS_NOT_VALID, exception.getMessage());

            verify(item, never()).getPoster();
        }

        @ParameterizedTest
        @EnumSource(value = ItemStatus.class, names = {"DELETED", "RESERVED", "DRAFT"})
        void updateItem_shouldThrowWhenItemStatusIsActiveAndRequestItemStatusIsInvalid(ItemStatus status) {
            request.setStatus(status);
            request.setImages(List.of());

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.STATUS_NOT_VALID, exception.getMessage());

            verify(item, never()).getPoster();
        }

        @Test
        void updateItem_shouldThrowWhenUserIsNotTheOwner() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            request.setImages(List.of());
            request.setStatus(ItemStatus.ACTIVE);

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(item.getPoster()).thenReturn(otherUser);

            AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());

            verifyNoInteractions(categoryRepository);
        }

        @Test
        void updateItem_shouldThrowWhenCategoryDoesNotExist() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            request.setImages(List.of());
            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 1);


            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(item.getPoster()).thenReturn(loggedInUser);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.CATEGORY_NOT_FOUND, exception.getMessage());

            verifyNoInteractions(geodbLocationRepository);
        }

        @Test
        void updateItem_shouldThrowWhenLocationDoesNotExist() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            request.setImages(List.of());
            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 1);
            request.setLocationId(1);

            Category category = mock(Category.class);

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(item.getPoster()).thenReturn(loggedInUser);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

            when(geodbLocationRepository.findById(request.getLocationId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.LOCATION_NOT_FOUND, exception.getMessage());

            verifyNoInteractions(itemImageRepository);
        }

        @Test
        void updateItem_shouldThrowWhenImageSavingFails() throws IOException {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 1);
            request.setLocationId(1);

            Category category = mock(Category.class);
            GeodbLocation location = mock(GeodbLocation.class);
            MultipartFile image = mock(MultipartFile.class);

            request.setImages(List.of(image));

            when(image.isEmpty()).thenReturn(false);
            when(image.getBytes()).thenThrow(new IOException("IOException"));

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(item.getPoster()).thenReturn(loggedInUser);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

            when(geodbLocationRepository.findById(request.getLocationId())).thenReturn(Optional.of(location));

            ImageSavingException exception = assertThrows(ImageSavingException.class,
                    () -> itemService.updateItem(itemId, request, authentication));
            assertEquals(ExceptionMessage.ERROR_SAVING_IMAGE, exception.getMessage());

            verify(dataChangePublisher, never()).publish(any(), any(), any());
        }

        @Test
        void updateItem_shouldUpdateItem() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            request.setStatus(ItemStatus.ACTIVE);
            request.setCategoryId((short) 1);
            request.setLocationId(1);
            request.setImages(List.of());

            Category category = mock(Category.class);
            GeodbLocation location = mock(GeodbLocation.class);

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(item.getPoster()).thenReturn(loggedInUser);

            when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

            when(geodbLocationRepository.findById(request.getLocationId())).thenReturn(Optional.of(location));

            boolean result = itemService.updateItem(itemId, request, authentication);

            assertTrue(result);

            ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
            verify(itemRepository).save(itemCaptor.capture());

            verify(dataChangePublisher).publish(Entity.ITEM, Action.UPDATE, itemCaptor.getValue());
        }
    }
}
