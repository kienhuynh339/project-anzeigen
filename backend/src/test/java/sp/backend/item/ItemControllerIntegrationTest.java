package sp.backend.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.dto.ErrorResponse;
import sp.backend.dto.ItemResponse;
import sp.backend.dto.ItemWithDistanceResponse;
import sp.backend.exception.ExceptionMessage;
import sp.backend.item_category.Category;
import sp.backend.item_category.CategoryRepository;
import sp.backend.item_image.ItemImageRepository;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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

    private final Object[] ITEM_RESPONSE_1_FAVORITE = {1L, (short) 2, 1L, "Anzeige 1", "Beschreibung...", 100, 26488, ItemStatus.RESERVED, OffsetDateTime.parse("2026-09-01T08:00:00+02").toInstant(), true};
    private final Object[] ITEM_RESPONSE_1_NOT_FAVORITE = {1L, (short) 2, 1L, "Anzeige 1", "Beschreibung...", 100, 26488, ItemStatus.RESERVED, OffsetDateTime.parse("2026-09-01T08:00:00+02").toInstant(), false};

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
            asserting(null, ITEM_RESPONSE_1_NOT_FAVORITE);
        }

        @Test
        void findById_shouldReturnItemResponseWithFavoriteFalseWhenUserIsSamwise() {
            asserting(UserEnum.SAMWISE, ITEM_RESPONSE_1_NOT_FAVORITE);
        }

        @Test
        void findById_shouldReturnItemResponseWithFavoriteTrueWhenUserIsFrodo() {
            asserting(UserEnum.FRODO, ITEM_RESPONSE_1_FAVORITE);
        }

        private void asserting(UserEnum userEnum, Object[] expected) {
            ResponseEntity<ItemResponse> response = tRTemplate(userEnum)
                    .getForEntity("/api/items/{itemId}", ItemResponse.class, 1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            assertItemResponse(response.getBody(), expected);
        }
    }


    // ---------------------------------------------------------
    // findByIdToEdit()
    // ---------------------------------------------------------

    @Nested
    class FindByIdToEditTests {
        @Test
        void findByIdToEdit_shouldThrowWhenUnauthenticated() {
            ResponseEntity<ErrorResponse> response = tRTemplate()
                    .getForEntity("/api/items/{itemId}/edit", ErrorResponse.class, 1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void findByIdToEdit_shouldThrowWhenItemDoesNotExist() {
            ResponseEntity<ErrorResponse> response = tRTemplate(UserEnum.SAMWISE)
                    .getForEntity("/api/items/{itemId}/edit", ErrorResponse.class, 99);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ITEM_NOT_FOUND);
        }

        @Test
        void findByIdToEdit_shouldThrowWhenNotOwner() {
            ResponseEntity<ErrorResponse> response = tRTemplate(UserEnum.MERRY)
                    .getForEntity("/api/items/{itemId}/edit", ErrorResponse.class, 1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCESS_DENIED);
        }

        @Test
        void findByIdToEdit_shouldReturnItemResponseWhenUserIsSamwise() {
            ResponseEntity<ItemResponse> response = tRTemplate(UserEnum.SAMWISE)
                    .getForEntity("/api/items/{itemId}/edit", ItemResponse.class, 1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            assertItemResponse(response.getBody(), ITEM_RESPONSE_1_NOT_FAVORITE);
        }
    }

    // ---------------------------------------------------------
    // search()
    // ---------------------------------------------------------

    @Nested
    class SearchTests {
        private final Object[] ITEM_2_WITH_DISTANCE_NULL_NOT_FAVORITE = {
                2L,
                1L,
                "Anzeige 2",
                "Beschreibung...",
                101,
                26488,
                OffsetDateTime.parse("2026-09-01T08:15:00+02").toInstant(),
                null,
                false};

        @Test
        void search_withoutParameters_shouldReturn5Items() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("page", 0).queryParam("size", 10).build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            // 5, da "Anzeige 1" reserviert ist
            assertEquals(5, items.size());
        }


        @Test
        void search_withSearchTerm_anzeige2_shouldReturn_item2() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("searchTerm", "Anzeige 2").queryParam("page", 0).queryParam("size", 10).build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            assertItemWithDistanceResponse(items.getFirst(), ITEM_2_WITH_DISTANCE_NULL_NOT_FAVORITE);
        }


        @Test
        void search_withCategoryId_2_shouldReturn_items_2_3() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("categoryId", 2).queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            // Anzeige 1 ist reserviert -> 2 statt 3
            assertEquals(2, items.size());
            assertEquals(2L, items.get(0).getId());
            assertEquals(3L, items.get(1).getId());
        }

        @Test
        void search_withMinPrice_102_andMaxPrice_300_shouldReturn_items_3_4_5() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("minPrice", 102).queryParam("maxPrice", 300).queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            assertEquals(3, items.size());
            assertEquals(3L, items.get(0).getId());
            assertEquals(4L, items.get(1).getId());
            assertEquals(5L, items.get(2).getId());
        }

        @Test
        void search_withLocationId_26488_shouldReturn_items_2_3() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("locationId", 26488).queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            assertEquals(2, items.size());
            assertEquals(2L, items.get(0).getId());
            assertEquals(3L, items.get(1).getId());
        }

        @Test
        void search_withLocationId_26488_andRadiusKm_29_shouldReturn_items_2_3() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("locationId", 26488).queryParam("radiusKm", 29).queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            assertEquals(2, items.size());
            assertEquals(2L, items.get(0).getId());
            assertEquals(3L, items.get(1).getId());
        }

        @Test
        void search_withLocationId_26488_andRadiusKm_30_shouldReturn_items_2_3_4() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("locationId", 26488).queryParam("radiusKm", 30).queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            assertEquals(3, items.size());
            assertEquals(2L, items.get(0).getId());
            assertEquals(3L, items.get(1).getId());
            // Entfernung zu 26488 = 29,5
            assertEquals(4L, items.get(2).getId());

            Double distanceFrom4To26488 = items.get(2).getDistance();
            assertTrue(29 <= distanceFrom4To26488 && distanceFrom4To26488 <= 30);
        }

        @Test
        void search_withLocationId_26488_andRadiusKm_40_shouldReturn_items_2_3_4() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("locationId", 26488).queryParam("radiusKm", 40).queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            assertEquals(3, items.size());
            assertEquals(2L, items.get(0).getId());
            assertEquals(3L, items.get(1).getId());
            // Entfernung zu 26488 = 29,5
            assertEquals(4L, items.get(2).getId());
        }

        @Test
        void search_withLocationId_26488_andRadiusKm_41_shouldReturn_items_2_3_4_5_6() {
            URI uri = UriComponentsBuilder.fromPath("/api/items").queryParam("locationId", 26488).queryParam("radiusKm", 41).queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ItemWithDistanceResponse> items = findItems(uri, ItemWithDistanceResponse.class);

            assertEquals(5, items.size());
            assertEquals(2L, items.get(0).getId());
            assertEquals(3L, items.get(1).getId());

            // Entfernung zu 26488 = 29,5
            assertEquals(4L, items.get(2).getId());

            Double distanceFrom4To26488 = items.get(2).getDistance();
            assertTrue(29 <= distanceFrom4To26488 && distanceFrom4To26488 <= 30);

            // Entfernung zu 26488 = 40,5
            assertEquals(5L, items.get(3).getId());

            Double distanceFrom5To26488 = items.get(3).getDistance();
            assertTrue(40 <= distanceFrom5To26488 && distanceFrom5To26488 <= 41);

            assertEquals(6L, items.get(4).getId());
        }
    }

    // ---------------------------------------------------------
    // findByUserId()
    // ---------------------------------------------------------

    @Nested
    class FindByUserIdTests {
        private final URI uri = UriComponentsBuilder.fromPath("/api/items/user/3").queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

        @Test
        void findByUserId_userDoesNotExist_shouldThrow() {
            ResponseEntity<ErrorResponse> response = tRTemplate()
                    .getForEntity("/api/items/user/{userId}", ErrorResponse.class, 99);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_FOUND);
        }

        @Test
        void findByUserId_withUserId_3_shouldReturn_items_5_6() {
            List<ItemResponse> items = findItems(uri, ItemResponse.class);

            assertEquals(2, items.size());
            assertEquals(5L, items.get(0).getId());
            assertEquals(6L, items.get(1).getId());
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
    private <T> List<T> findItems(URI uri, Class<T> responseType) {
        ResponseEntity<JsonNode> response = tRTemplate()
                .exchange(uri,
                        HttpMethod.GET,
                        null,
                        JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = response.getBody();
        assertNotNull(body);

        return objectMapper.readerForListOf(responseType)
                .readValue(body.get("content"));
    }

    private void assertItemResponse(ItemResponse itemResponse, Object[] expected) {
        assertThat(itemResponse).isNotNull().extracting(ItemResponse::getId,
                        ItemResponse::getCategoryId,
                        i -> i.getPoster().getId(),
                        ItemResponse::getTitle,
                        ItemResponse::getDescription,
                        ItemResponse::getPrice,
                        i -> i.getLocation().getId(),
                        ItemResponse::getStatus,
                        i -> i.getCreatedAt().toInstant(),
                        ItemResponse::getFavorite)
                .containsExactly(expected);
    }

    private void assertItemWithDistanceResponse(ItemWithDistanceResponse itemWithDistanceResponse, Object[] expected) {
        assertThat(itemWithDistanceResponse).isNotNull().extracting(ItemWithDistanceResponse::getId,
                        ItemWithDistanceResponse::getPosterId,
                        ItemWithDistanceResponse::getTitle,
                        ItemWithDistanceResponse::getDescription,
                        ItemWithDistanceResponse::getPrice,
                        i -> i.getLocation().getId(),
                        i -> i.getCreatedAt().toInstant(),
                        ItemWithDistanceResponse::getDistance,
                        ItemWithDistanceResponse::getFavorite)
                .containsExactly(expected);
    }

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
