package sp.backend.item_category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.TestUtils;
import sp.backend.chat.Chat;
import sp.backend.chat.ChatRepository;
import sp.backend.chat_message.Message;
import sp.backend.chat_message.MessageRepository;
import sp.backend.dto.*;
import sp.backend.exception.ExceptionMessage;
import sp.backend.item.ItemRepository;
import sp.backend.item.ItemStatus;
import sp.backend.item_category.Category;
import sp.backend.item_category.CategoryRepository;
import sp.backend.item_image.ItemImage;
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
import static org.junit.jupiter.api.Assertions.*;
import static sp.backend.TestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class CategoryControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;


    // ---------------------------------------------------------
    // findAll()
    // ---------------------------------------------------------

    @Test
    void findAll_shouldReturnALlCategories() {
        URI uri = UriComponentsBuilder.fromPath("/api/categories").build().encode().toUri();

        List<Category> categories = findCategories(uri, Category.class);

        assertNotNull(categories);
        assertEquals(4, categories.size());
        assertEquals("Alle Kategorien", categories.get(0).getName());
        assertEquals("Kategorie 1", categories.get(1).getName());
        assertEquals("Kategorie 2", categories.get(2).getName());
        assertEquals("Kategorie 3", categories.get(3).getName());
    }

    private void assertCategory(Category category, Object[] expected) {
        assertThat(category).isNotNull().extracting(
                        Category::getId,
                        Category::getName)
                .containsExactly(expected);
    }

    private <T> List<T> findCategories(URI uri, Class<T> responseType) {
        ResponseEntity<JsonNode> response = tRTemplate()
                .exchange(uri,
                        HttpMethod.GET,
                        null,
                        JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = response.getBody();
        assertNotNull(body);

        return objectMapper.readerForListOf(responseType)
                .readValue(body);
    }

    private TestRestTemplate tRTemplate() {
        return TestUtils.tRTemplate(restTemplate);
    }

    private TestRestTemplate tRTemplate(UserEnum userEnum) {
        return TestUtils.tRTemplate(restTemplate, userEnum);
    }
}
