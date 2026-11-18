/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_image;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import sp.backend.TestUtils;
import sp.backend.dto.*;
import sp.backend.exception.ExceptionMessage;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sp.backend.TestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class ItemImageControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemImageRepository itemImageRepository;

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Nested
    class FindByIdTests {
        @Test
        void findById_imageDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/images/{imageId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            9);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.IMAGE_NOT_FOUND);
        }

        @Test
        void findById_shouldReturnItemImage() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<byte[]> response = template
                    .exchange("/api/images/{imageId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            byte[].class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            assertThat(response.getHeaders().getContentType())
                    .isEqualTo(MediaType.IMAGE_JPEG);

            assertThat(response.getBody()).isNotEmpty();
            byte[] expectedImage = itemImageRepository.findById(1L).orElseThrow().getImage();
            assertThat(response.getBody()).isEqualTo(expectedImage);
        }
    }

    // ---------------------------------------------------------
    // findFirstByItemId()
    // ---------------------------------------------------------

    @Nested
    class FindFirstByItemIdTests {
        @Test
        void findFirstByItemId_shouldReturnNoContent() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<byte[]> response = template
                    .exchange("/api/images/item/{itemId}/first",
                            HttpMethod.GET,
                            requestEntity(template),
                            byte[].class,
                            8);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();
        }

        @Test
        void findFirstByItemId_shouldReturnItemImage() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<byte[]> response = template
                    .exchange("/api/images/item/{itemId}/first",
                            HttpMethod.GET,
                            requestEntity(template),
                            byte[].class,
                            4);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            assertThat(response.getHeaders().getContentType())
                    .isEqualTo(MediaType.IMAGE_JPEG);

            assertThat(response.getBody()).isNotEmpty();
            byte[] expectedImage = itemImageRepository.findById(4L).orElseThrow().getImage();
            assertThat(response.getBody()).isEqualTo(expectedImage);
        }
    }

    // ---------------------------------------------------------
    // findImageIdsByItemId()
    // ---------------------------------------------------------

    @Nested
    class FindImageIdsByItemIdTests {
        @Test
        void findImageIdsByItemId_shouldReturnNoContent() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<JsonNode> response = template
                    .exchange("/api/images/item/{itemId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            JsonNode.class,
                            8);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();
        }

        @Test
        void findImageIdsByItemId_shouldReturnImageIds() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<JsonNode> response = template
                    .exchange("/api/images/item/{itemId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            JsonNode.class,
                            4);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            JsonNode body = response.getBody();
            assertNotNull(body);

            List<Long> imageIds = objectMapper.readerForListOf(Long.class).readValue(body);
            assertEquals(3, imageIds.size());
            assertEquals(4L, imageIds.get(0));
            assertEquals(5L, imageIds.get(1));
            assertEquals(6L, imageIds.get(2));

        }
    }

    private TestRestTemplate tRTemplate() {
        return TestUtils.tRTemplate(restTemplate);
    }
}
