package sp.backend.item_image;

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
public class ItemImageServiceTest {
    @Autowired
    private ItemImageService itemImageService;

    @MockitoBean
    private ItemImageRepository itemImageRepository;

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Nested
    class FindByIdTests {
        private final Long imageId = 1L;

        @Test
        void findById_shouldThrowWhenImageDoesNotExist() {
            when(itemImageRepository.findById(imageId)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> itemImageService.findById(imageId));
            assertEquals(ExceptionMessage.IMAGE_NOT_FOUND, exception.getMessage());
        }

        @Test
        void findById_shouldReturnItemImage() {
            ItemImage itemImage = mock(ItemImage.class);
            when(itemImage.getId()).thenReturn(1L);

            when(itemImageRepository.findById(imageId)).thenReturn(Optional.of(itemImage));

            ItemImage result = itemImageService.findById(imageId);

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }
    }

    // ---------------------------------------------------------
    // findFirstByItemId()
    // ---------------------------------------------------------

    @Nested
    class FindFirstByItemIdTests {
        private final Long itemId = 1L;

        @Test
        void findFirstByItemId_shouldReturnItemImage() {
            ItemImage itemImage = mock(ItemImage.class);
            when(itemImage.getId()).thenReturn(1L);

            when(itemImageRepository.findFirstByItemIdOrderByDisplayOrderAsc(itemId)).thenReturn(itemImage);

            ItemImage result = itemImageService.findFirstByItemId(itemId);

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }
    }

    // ---------------------------------------------------------
    // findImageIdsByItemId()
    // ---------------------------------------------------------

    @Nested
    class FindImageIdsByItemIdTests {
        private final Long itemId = 1L;

        @Test
        void findImageIdsByItemId_shouldReturnImageIds() {
            ItemImage image1 = mock(ItemImage.class);
            ItemImage image2 = mock(ItemImage.class);

            when(image1.getId()).thenReturn(1L);
            when(image2.getId()).thenReturn(2L);

            when(itemImageRepository.findByItemIdOrderByDisplayOrderAsc(itemId)).thenReturn(List.of(image1, image2));

            List<Long> result = itemImageService.findImageIdsByItemId(itemId);

            assertNotNull(result);
            assertEquals(List.of(1L, 2L), result);
        }
    }

}
