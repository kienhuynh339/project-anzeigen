package sp.backend.item_favorite;

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
public class ItemFavoriteControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;

    

    private void assertItemFavorite(MessageResponse messageResponse, Object[] expected) {
        assertThat(messageResponse).isNotNull().extracting(
                        MessageResponse::getId,
                        MessageResponse::getSenderId,
                        MessageResponse::getContent,
                        MessageResponse::getRead,
                        m -> m.getCreatedAt().toInstant())
                .containsExactly(expected);
    }

    private <T> List<T> findMessages(UserEnum user, URI uri, Class<T> responseType) {
        TestRestTemplate template = tRTemplate(user);

        ResponseEntity<JsonNode> response = tRTemplate(user)
                .exchange(uri,
                        HttpMethod.GET,
                        requestEntity(template),
                        JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = response.getBody();
        assertNotNull(body);

        return objectMapper.readerForListOf(responseType)
                .readValue(body.get("content"));
    }

    private TestRestTemplate tRTemplate() {
        return TestUtils.tRTemplate(restTemplate);
    }

    private TestRestTemplate tRTemplate(UserEnum userEnum) {
        return TestUtils.tRTemplate(restTemplate, userEnum);
    }
}
