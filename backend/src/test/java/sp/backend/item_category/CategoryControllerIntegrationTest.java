/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.TestUtils;
import sp.backend.dto.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
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
