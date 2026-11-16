package sp.backend.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import sp.backend.dto.ErrorResponse;
import sp.backend.dto.ItemResponse;
import sp.backend.exception.ExceptionMessage;
import sp.backend.item_category.Category;
import sp.backend.item_category.CategoryRepository;
import sp.backend.item_image.ItemImageRepository;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

import java.time.OffsetDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GeodbLocationRepository geodbLocationRepository;

    @Autowired
    private ItemImageRepository itemImageRepository;

//    @Autowired
//    private TestEntityManager entityManager;

    private User user;
    private User anotherUser;
    private Category category;
    private GeodbLocation location;

    private Long itemId;


    @BeforeEach
    void setUp() {
        // Testdaten vorbereiten
    }

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Nested
    class FindByIdTests {
        @Test
        void findById_shouldThrowWhenItemDoesNotExist() {
            ResponseEntity<ErrorResponse> response = tRTemplate()
                    .getForEntity("/api/items/{itemId}", ErrorResponse.class, 99);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ITEM_NOT_FOUND);
        }

        @Test
        void findById_shouldReturnItemResponseWithFavoriteFalseWhenUnauthenticated() {
            asserting(null, false);
        }

        @Test
        void findById_shouldReturnItemResponseWithFavoriteFalseWhenUserIsSamwise() {
            asserting(UserEnum.SAMWISE, false);
        }

        @Test
        void findById_shouldReturnItemResponseWithFavoriteTrueWhenUserIsFrodo() {
            asserting(UserEnum.FRODO, true);
        }

        private void asserting(UserEnum userEnum, Boolean isFavorite) {
            ResponseEntity<ItemResponse> response = tRTemplate(userEnum)
                    .getForEntity("/api/items/{itemId}", ItemResponse.class, 1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            assertThat(response.getBody()).isNotNull().extracting(
                    ItemResponse::getId,
                    ItemResponse::getCategoryId,
                    i -> i.getPoster().getId(),
                    ItemResponse::getTitle,
                    ItemResponse::getDescription,
                    ItemResponse::getPrice,
                    i -> i.getLocation().getId(),
                    ItemResponse::getStatus,
                    i -> i.getCreatedAt().toInstant(),
                    ItemResponse::getFavorite).containsExactly(
                    1L, (short) 2, 1L, "Anzeige 1", "Beschreibung...", 100, 26488, ItemStatus.RESERVED, OffsetDateTime.parse("2026-09-01T08:00:00+02").toInstant(), isFavorite
            );
        }
    }


    // ---------------------------------------------------------
    // findByIdToEdit()
    // ---------------------------------------------------------

    @Nested
    class FindByIdToEditTests {
        @Test
        void findByIdToEdit_shouldThrowWhenUnauthenticated() {
            ResponseEntity<ItemResponse> response = tRTemplate()
                    .getForEntity("/api/items/{itemId}/edit", ItemResponse.class, 1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
        }

    }


//
//    @Test
//    void search_shouldReturnItems() {
//
//        ResponseEntity<RestResponsePage<ItemWithDistanceResponse>> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items",
//                                HttpMethod.GET,
//                                null,
//                                new ParameterizedTypeReference<>() {
//                                }
//                        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//    }
//
//    public class RestResponsePage<T> extends PageImpl<T> {
//
//        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
//        public RestResponsePage(
//                @JsonProperty("content") List<T> content,
//                @JsonProperty("number") int number,
//                @JsonProperty("size") int size,
//                @JsonProperty("totalElements") long totalElements,
//                @JsonProperty("pageable") JsonNode pageable,
//                @JsonProperty("last") boolean last,
//                @JsonProperty("totalPages") int totalPages,
//                @JsonProperty("sort") JsonNode sort,
//                @JsonProperty("first") boolean first,
//                @JsonProperty("numberOfElements") int numberOfElements,
//                @JsonProperty("empty") boolean empty
//        ) {
//            super(content, PageRequest.of(number, size), totalElements);
//        }
//    }
//
//    @Test
//    void search_shouldApplyAllFilters() {
//
//        String url = UriComponentsBuilder
//                .fromPath("/api/items")
//                .queryParam("searchTerm", "Fahrrad")
//                .queryParam("categoryId", category.getId())
//                .queryParam("minPrice", 100)
//                .queryParam("maxPrice", 500)
//                .queryParam("locationId", location.getId())
//                .queryParam("radiusKm", 20)
//                .queryParam("page", 0)
//                .queryParam("size", 10)
//                .toUriString();
//
//        ResponseEntity<RestResponsePage<ItemWithDistanceResponse>> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                url,
//                                HttpMethod.GET,
//                                null,
//                                new ParameterizedTypeReference<>() {
//                                }
//                        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//    }
//
//    @Test
//    void findByUserId_shouldReturnItemsOfUser() {
//
//        ResponseEntity<RestResponsePage<ItemResponse>> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/user/" + user.getId(),
//                                HttpMethod.GET,
//                                null,
//                                new ParameterizedTypeReference<>() {
//                                }
//                        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//    }
//
//    @Test
//    void findByUserId_shouldReturn404_whenUserDoesNotExist() {
//
//        ResponseEntity<String> response =
//                trTemplate(User.SAMWISE)
//                        .getForEntity(
//                                "/api/items/user/999999",
//                                String.class
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    void findFavoriteItems_shouldReturnFavorites() {
//
//        ResponseEntity<RestResponsePage<ItemResponse>> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/user/favorites",
//                                HttpMethod.GET,
//                                null,
//                                new ParameterizedTypeReference<>() {
//                                }
//                        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//    }
//
//    @Test
//    void markItemAsDeleted_shouldSetStatusToDeleted() {
//
//        ResponseEntity<Void> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/{itemId}",
//                                HttpMethod.DELETE,
//                                null,
//                                Void.class,
//                                itemId
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.NO_CONTENT);
//
//        Item deletedItem = itemRepository.findById(itemId).orElseThrow();
//
//        assertThat(deletedItem.getStatus()).isEqualTo(ItemStatus.DELETED);
//    }
//
//    @Test
//    void markItemAsDeleted_shouldReturn403_whenUserIsNotOwner() {
//
//        ResponseEntity<String> response =
//                authenticatedClient(anotherUser.getAccountName())
//                        .exchange(
//                                "/api/items/{itemId}",
//                                HttpMethod.DELETE,
//                                null,
//                                String.class,
//                                itemId
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.FORBIDDEN);
//    }
//
//
//    @Test
//    void markItemAsDeleted_shouldReturn404_whenItemDoesNotExist() {
//
//        ResponseEntity<String> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/999999",
//                                HttpMethod.DELETE,
//                                null,
//                                String.class
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    void updateItemStatus_shouldUpdateStatus() {
//
//        ItemStatusUpdateRequest request = new ItemStatusUpdateRequest();
//        request.setStatus(ItemStatus.RESERVED);
//
//        ResponseEntity<Void> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/{itemId}",
//                                HttpMethod.PATCH,
//                                new HttpEntity<>(request),
//                                Void.class,
//                                itemId
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.NO_CONTENT);
//
//        Item updatedItem = itemRepository.findById(itemId).orElseThrow();
//
//        assertThat(updatedItem.getStatus()).isEqualTo(ItemStatus.RESERVED);
//    }
//
//    @Test
//    void updateItemStatus_shouldReturn403_whenUserIsNotOwner() {
//
//        ItemStatusUpdateRequest request = new ItemStatusUpdateRequest();
//        request.setStatus(ItemStatus.RESERVED);
//
//        ResponseEntity<String> response =
//                authenticatedClient(anotherUser.getAccountName())
//                        .exchange(
//                                "/api/items/{itemId}",
//                                HttpMethod.PATCH,
//                                new HttpEntity<>(request),
//                                String.class,
//                                itemId
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.FORBIDDEN);
//    }
//
//    @Test
//    void updateItemStatus_shouldReturn400_whenStatusIsInvalid() {
//
//        ItemStatusUpdateRequest request = new ItemStatusUpdateRequest();
//        request.setStatus(ItemStatus.DRAFT);
//
//        ResponseEntity<String> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/{itemId}",
//                                HttpMethod.PATCH,
//                                new HttpEntity<>(request),
//                                String.class,
//                                itemId
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.BAD_REQUEST);
//    }
//
//    @Test
//    void updateItemStatus_shouldReturnError_whenItemIsAlreadyDeleted() {
//
//        Item item = itemRepository.findById(itemId).orElseThrow();
//        item.setStatus(ItemStatus.DELETED);
//        itemRepository.save(item);
//
//        ItemStatusUpdateRequest request = new ItemStatusUpdateRequest();
//        request.setStatus(ItemStatus.ACTIVE);
//
//        ResponseEntity<String> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/{itemId}",
//                                HttpMethod.PATCH,
//                                new HttpEntity<>(request),
//                                String.class,
//                                itemId
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.BAD_REQUEST);
//    }
//
//    @Test
//    void createItem_shouldCreateItem() {
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//
//        body.add("title", "Test Fahrrad");
//        body.add("description", "Ein gebrauchtes Fahrrad");
//        body.add("price", "250");
//        body.add("categoryId", category.getId().toString());
//        body.add("locationId", location.getId().toString());
//        body.add("postcode", "52062");
//        body.add("locationExtraInfo", "Innenstadt");
//        body.add("status", ItemStatus.ACTIVE.name());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        HttpEntity<MultiValueMap<String, Object>> request =
//                new HttpEntity<>(body, headers);
//
//        ResponseEntity<Void> response =
//                trTemplate(User.SAMWISE)
//                        .postForEntity(
//                                "/api/items",
//                                request,
//                                Void.class
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.CREATED);
//
//        assertThat(response.getHeaders().getLocation()).isNotNull();
//
//        String locationHeader = response.getHeaders().getLocation().toString();
//
//        assertThat(locationHeader).contains("/api/items/");
//
//        Item createdItem = itemRepository.findAll().stream()
//                .filter(item -> item.getTitle().equals("Test Fahrrad"))
//                .findFirst()
//                .orElseThrow();
//
//        assertThat(createdItem.getStatus()).isEqualTo(ItemStatus.ACTIVE);
//        assertThat(createdItem.getPoster().getId()).isEqualTo(user.getId());
//    }
//
//    @Test
//    void updateItem_shouldUpdateExistingItem() {
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//
//        body.add("title", "Aktualisiertes Fahrrad");
//        body.add("description", "Aktualisierte Beschreibung");
//        body.add("price", "300");
//        body.add("categoryId", category.getId().toString());
//        body.add("locationId", location.getId().toString());
//        body.add("postcode", "52062");
//        body.add("locationExtraInfo", "Neue Information");
//        body.add("status", ItemStatus.ACTIVE.name());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        HttpEntity<MultiValueMap<String, Object>> request =
//                new HttpEntity<>(body, headers);
//
//        ResponseEntity<Void> response =
//                trTemplate(User.SAMWISE)
//                        .exchange(
//                                "/api/items/{itemId}",
//                                HttpMethod.PUT,
//                                request,
//                                Void.class,
//                                itemId
//                        );
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(HttpStatus.NO_CONTENT);
//
//        Item updatedItem = itemRepository.findById(itemId).orElseThrow();
//
//        assertThat(updatedItem.getTitle())
//                .isEqualTo("Aktualisiertes Fahrrad");
//
//        assertThat(updatedItem.getPrice()).isEqualTo(300);
//    }
//


    enum UserEnum {SAMWISE, MERRY, PIPPIN, FRODO,}

    private TestRestTemplate tRTemplate() {
        return restTemplate;
    }

    private TestRestTemplate tRTemplate(UserEnum userEnum) {
        if (userEnum == null) return restTemplate;
        return switch (userEnum) {
            case SAMWISE -> restTemplate.withBasicAuth("samwise", "sam123456");
            case MERRY -> restTemplate.withBasicAuth("merry", "mer123456");
            case PIPPIN -> restTemplate.withBasicAuth("pippin", "pip123456");
            case FRODO -> restTemplate.withBasicAuth("frodo", "fro123456");
            default -> restTemplate;
        };
    }
}
