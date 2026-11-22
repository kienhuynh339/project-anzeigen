/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_image;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sp.backend.dto.*;
import sp.backend.exception.ExceptionMessage;

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
        void findById_imageDoesNotExist_shouldThrow() {
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
