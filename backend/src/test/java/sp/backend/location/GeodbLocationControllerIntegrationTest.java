/**
 * @author Trung Kien Huynh
 */
package sp.backend.location;

import org.junit.jupiter.api.Nested;
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
public class GeodbLocationControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------------------------------------------------
    // findByPostcode()
    // ---------------------------------------------------------

    @Nested
    class FindByPostcodeTests {
        @Test
        void findByPostcode_shouldReturnNoContent() {
            URI uri = UriComponentsBuilder.fromPath("/api/locations").queryParam("postcode", "00000").queryParam("page", 0).queryParam("size", 10).queryParam("sort", "name,asc").build().encode().toUri();

            TestRestTemplate template = tRTemplate();

            ResponseEntity<JsonNode> response = template
                    .exchange(uri,
                            HttpMethod.GET,
                            requestEntity(template),
                            JsonNode.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();
        }

        @Test
        void findByPostcode_shouldReturnGeodbLocationResponses() {
            URI uri = UriComponentsBuilder.fromPath("/api/locations").queryParam("postcode", "15848").queryParam("page", 0).queryParam("size", 10).queryParam("sort", "name,asc").build().encode().toUri();

            TestRestTemplate template = tRTemplate();

            ResponseEntity<JsonNode> response = template
                    .exchange(uri,
                            HttpMethod.GET,
                            requestEntity(template),
                            JsonNode.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            JsonNode body = response.getBody();
            assertNotNull(body);

            List<GeodbLocationResponse> locations = objectMapper.readerForListOf(GeodbLocationResponse.class).readValue(body);
            assertEquals(2, locations.size());
            assertThat(locations.get(0).getName()).isEqualTo("Friedland");
            assertThat(locations.get(0).getExtraInfo()).isEqualTo("(Stadt / Gemeinde) - Brandenburg");
            assertThat(locations.get(1).getName()).isEqualTo("Friedland");
            assertThat(locations.get(1).getExtraInfo()).isEqualTo("(Orts- / Stadtteil) - Brandenburg");
        }
    }

    private TestRestTemplate tRTemplate() {
        return TestUtils.tRTemplate(restTemplate);
    }
}
