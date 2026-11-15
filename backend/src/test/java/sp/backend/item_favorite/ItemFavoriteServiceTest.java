package sp.backend.item_favorite;

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
import sp.backend.exception.*;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
import sp.backend.item.ItemStatus;
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
public class ItemFavoriteServiceTest {
    @Autowired
    private ItemFavoriteService itemFavoriteService;

    @MockitoBean
    private ItemFavoriteRepository itemFavoriteRepository;

    @MockitoBean
    private ItemRepository itemRepository;

    @MockitoBean
    private UserRepository userRepository;

    private Authentication authentication;
    private User loggedInUser;
    private User otherUser;
    private Item item;
    private ItemFavorite itemFavorite;
    private final Pageable pageable = PageRequest.of(0, 10);


    @BeforeEach
    void setUp() {
        loggedInUser = mock(User.class);
        otherUser = mock(User.class);
        item = mock(Item.class);
        itemFavorite = mock(ItemFavorite.class);

        when(loggedInUser.getId()).thenReturn(1L);
        when(loggedInUser.getAccountName()).thenReturn("loggedInUser");
        when(userRepository.findByAccountName(loggedInUser.getAccountName())).thenReturn(Optional.of(loggedInUser));

        authentication = new UsernamePasswordAuthenticationToken(loggedInUser.getAccountName(), "123456");

        when(otherUser.getId()).thenReturn(2L);
        when(otherUser.getAccountName()).thenReturn("otherUser");

        when(item.getId()).thenReturn(1L);

        when(itemFavorite.getId()).thenReturn(1L);
    }

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Nested
    class FindByIdTests {
        private Long itemFavoriteId;

        @BeforeEach
        void setUp() {
            itemFavoriteId = itemFavorite.getId();
        }

        @Test
        void findById_shouldThrowWhenUserIsUnauthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemFavoriteService.findById(itemFavoriteId, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemFavoriteRepository);
        }

        @Test
        void findById_shouldThrowWhenItemFavoriteDoesNotExist() {
            when(itemFavoriteRepository.findById(itemFavoriteId)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemFavoriteService.findById(itemFavoriteId, authentication));
            assertEquals(ExceptionMessage.FAVORITE_ITEM_NOT_FOUND, exception.getMessage());

            verify(itemFavorite, never()).getUser();
        }

        @Test
        void findById_shouldThrowWhenUserIsNotTheOwner() {
            when(itemFavoriteRepository.findById(itemFavoriteId)).thenReturn(Optional.of(itemFavorite));

            when(itemFavorite.getUser()).thenReturn(otherUser);

            AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                    () -> itemFavoriteService.findById(itemFavoriteId, authentication));
            assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());
        }

        @Test
        void findById_shouldReturnItemFavoriteResponse() {
            when(itemFavoriteRepository.findById(itemFavoriteId)).thenReturn(Optional.of(itemFavorite));

            when(itemFavorite.getItem()).thenReturn(item);

            when(itemFavorite.getUser()).thenReturn(loggedInUser);

            ItemFavoriteResponse response = itemFavoriteService.findById(itemFavoriteId, authentication);

            assertNotNull(response);
            assertEquals(item.getId(), response.getItemId());
        }
    }

    // ---------------------------------------------------------
    // createItemFavorite()
    // ---------------------------------------------------------

    @Nested
    class CreateItemFavoriteTests {
        private Long itemId;

        @BeforeEach
        void setUp() {
            itemId = item.getId();
        }

        @Test
        void createItemFavorite_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemFavoriteService.createItemFavorite(itemId, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void createItemFavorite_shouldThrowWhenItemDoesNotExist() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemFavoriteService.createItemFavorite(itemId, authentication));
            assertEquals(ExceptionMessage.ITEM_NOT_FOUND, exception.getMessage());

            verify(item, never()).getPoster();
        }

        @Test
        void createItemFavorite_shouldThrowWhenUserIsTheOwnerOfTheItem() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(loggedInUser);

            CannotFavorOwnItemException exception = assertThrows(CannotFavorOwnItemException.class,
                    () -> itemFavoriteService.createItemFavorite(itemId, authentication));
            assertEquals(ExceptionMessage.POSTER_CANNOT_FAVOR_OWN_ITEM, exception.getMessage());

            verify(item, never()).getStatus();
        }

        @Test
        void createItemFavorite_shouldThrowWhenItemIsDeleted() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(otherUser);

            when(item.getStatus()).thenReturn(ItemStatus.DELETED);

            CannotFavorDeletedItemException exception = assertThrows(CannotFavorDeletedItemException.class,
                    () -> itemFavoriteService.createItemFavorite(itemId, authentication));
            assertEquals(ExceptionMessage.CANNOT_FAVOR_DELETED_ITEM, exception.getMessage());

            verifyNoInteractions(itemFavoriteRepository);
        }

        @Test
        void createItemFavorite_shouldThrowWhenFavoriteItemAlreadyExists() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(otherUser);

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(itemFavoriteRepository.findByUser_IdAndItem_Id(loggedInUser.getId(), item.getId()))
                    .thenReturn(Optional.of(itemFavorite));

            FavoriteItemAlreadyExistsException exception = assertThrows(FavoriteItemAlreadyExistsException.class,
                    () -> itemFavoriteService.createItemFavorite(itemId, authentication));
            assertEquals(ExceptionMessage.FAVORITE_ITEM_ALREADY_EXISTS, exception.getMessage());

            verify(itemFavoriteRepository, never()).save(any());
        }

        @Test
        void createItemFavorite_shouldCreateItemFavorite() {
            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

            when(item.getPoster()).thenReturn(otherUser);

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(itemFavoriteRepository.findByUser_IdAndItem_Id(loggedInUser.getId(), item.getId()))
                    .thenReturn(Optional.empty());

            ItemFavorite itemFavorite = mock(ItemFavorite.class);

            when(itemFavoriteRepository.save(any(ItemFavorite.class))).thenReturn(itemFavorite);

            ItemFavorite result = itemFavoriteService.createItemFavorite(itemId, authentication);

            assertNotNull(result);
            verify(itemFavoriteRepository).save(any(ItemFavorite.class));
        }
    }

    // ---------------------------------------------------------
    // deleteItemFavorite()
    // ---------------------------------------------------------

    @Nested
    class DeleteItemFavoriteTests {
        private Long itemId;

        @BeforeEach
        void setUp() {
            itemId = item.getId();
        }

        @Test
        void deleteItemFavorite_shouldThrowWhenUserIsNotUnauthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> itemFavoriteService.deleteItemFavorite(itemId, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemFavoriteRepository);
        }

        @Test
        void deleteItemFavorite_shouldThrowWhenItemFavoriteDoesNotExist() {
            when(itemFavoriteRepository.findByUser_IdAndItem_Id(loggedInUser.getId(), itemId))
                    .thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemFavoriteService.deleteItemFavorite(itemId, authentication));
            assertEquals(ExceptionMessage.FAVORITE_ITEM_NOT_FOUND, exception.getMessage());

            verify(itemFavoriteRepository, never()).delete(any());
        }

        @Test
        void deleteItemFavorite_shouldDeleteItemFavorite() {
            when(itemFavoriteRepository.findByUser_IdAndItem_Id(loggedInUser.getId(), itemId))
                    .thenReturn(Optional.of(itemFavorite));

            itemFavoriteService.deleteItemFavorite(itemId, authentication);

            verify(itemFavoriteRepository).delete(itemFavorite);
        }
    }
}
