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
import sp.backend.exception.DeletedItemModificationNotAllowedException;
import sp.backend.exception.ExceptionMessage;
import sp.backend.exception.ImageSavingException;
import sp.backend.exception.ImageSizeExceededException;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
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

}
