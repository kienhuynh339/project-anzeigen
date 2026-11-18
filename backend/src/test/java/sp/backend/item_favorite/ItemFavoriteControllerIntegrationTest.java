/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import sp.backend.TestUtils;
import sp.backend.dto.*;
import sp.backend.exception.ExceptionMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sp.backend.TestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class ItemFavoriteControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemFavoriteRepository itemFavoriteRepository;

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Nested
    class FindByIdTests {
        @Test
        void findById_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites/{itemFavoriteId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void findById_itemFavoriteDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites/{itemFavoriteId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            99);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.FAVORITE_ITEM_NOT_FOUND);
        }

        @Test
        void findById_notOwner_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites/{itemFavoriteId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCESS_DENIED);
        }

        @Test
        void findById_shouldReturnItemFavoriteResponse() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ItemFavoriteResponse> response = template
                    .exchange("/api/item-favorites/{itemFavoriteId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ItemFavoriteResponse.class,
                            3);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().extracting(ItemFavoriteResponse::getItemId)
                    .isEqualTo(4L);
        }
    }


    // ---------------------------------------------------------
    // createItemFavorite()
    // ---------------------------------------------------------

    @Nested
    class CreateItemFavoriteTests {
        private ItemFavoriteCreateRequest requestBody;

        @BeforeEach
        void setUp() {
            requestBody = new ItemFavoriteCreateRequest();
            requestBody.setItemId(1L);
        }

        @Test
        void createItemFavorite_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void createItemFavorite_itemDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            requestBody.setItemId(9L);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ITEM_NOT_FOUND);
        }

        @Test
        void createItemFavorite_favorOwnItem_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            requestBody.setItemId(1L);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.POSTER_CANNOT_FAVOR_OWN_ITEM);
        }

        @Test
        void createItemFavorite_itemIsDeleted_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setItemId(7L);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.CANNOT_FAVOR_DELETED_ITEM);
        }

        @Test
        void createItemFavorite_itemFavoriteAlreadyExists_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setItemId(1L);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.FAVORITE_ITEM_ALREADY_EXISTS);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void createItemFavorite_shouldCreateItemFavorite() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setItemId(6L);

            ResponseEntity<Void> response = template
                    .exchange("/api/item-favorites",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).isNotNull().hasPath("/api/item-favorites/10");

            ItemFavorite itemFavorite = itemFavoriteRepository.findById(10L).orElseThrow();
            assertEquals(4L, itemFavorite.getUser().getId());
            assertEquals(6L, itemFavorite.getItem().getId());

        }
    }

    // ---------------------------------------------------------
    // deleteItemFavorite()
    // ---------------------------------------------------------

    @Nested
    class DeleteItemFavoriteTests {
        @Test
        void deleteItemFavorite_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites/item/{itemId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void deleteItemFavorite_itemFavoriteDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/item-favorites/item/{itemId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.FAVORITE_ITEM_NOT_FOUND);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void deleteItemFavorite_shouldDeleteItemFavorite() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<Void> response = template
                    .exchange("/api/item-favorites/item/{itemId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            Void.class,
                            4);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            ItemFavorite deletedItemFavorite = itemFavoriteRepository.findById(3L).orElse(null);
            assertNull(deletedItemFavorite);
        }
    }


    private TestRestTemplate tRTemplate() {
        return TestUtils.tRTemplate(restTemplate);
    }

    private TestRestTemplate tRTemplate(UserEnum userEnum) {
        return TestUtils.tRTemplate(restTemplate, userEnum);
    }
}
