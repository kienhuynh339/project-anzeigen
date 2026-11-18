package sp.backend.chat;


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
public class ChatControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;


    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Nested
    class FindByIdTests {
        @Test
        void findById_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            2);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void findById_chatDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            6);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.CHAT_NOT_FOUND);
        }

        @Test
        void findById_notParticipant_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            2);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCESS_DENIED);
        }

        @Test
        void findById_lastMessageDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            5);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.MESSAGE_NOT_FOUND);
        }

        @Test
        void findById_shouldReturnChatResponse() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ChatResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ChatResponse.class,
                            2);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            assertThat(response.getBody()).isNotNull();
            Object[] expected = {2L, 1L, "Anzeige 1", ItemStatus.RESERVED, 1L, 3L, 6L, null};
            assertChatResponse(response.getBody(), expected);
        }
    }


    // ---------------------------------------------------------
    // findByUser()
    // ---------------------------------------------------------

    @Nested
    class FindByUserTests {
        @Test
        void findByUser_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void findByUser_chatWithNoLastMessageExists_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.MESSAGE_NOT_FOUND);
        }

        @Test
        void findByUser_shouldReturnChatResponses() {
            URI uri = UriComponentsBuilder.fromPath("/api/chats").queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<ChatResponse> chats = findChats(UserEnum.SAMWISE, uri, ChatResponse.class);
            assertThat(chats).isNotNull();
            assertEquals(4, chats.size());
            assertEquals(1L, chats.get(0).getId());
            assertEquals(2L, chats.get(1).getId());
            assertEquals(3L, chats.get(2).getId());
            assertEquals(4L, chats.get(3).getId());
        }
    }


    // ---------------------------------------------------------
    // deleteChat()
    // ---------------------------------------------------------

    @Nested
    class DeleteChatTests {
        @Test
        void deleteChat_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            ErrorResponse.class,
                            2);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void deleteChat_chatDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            ErrorResponse.class,
                            6);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.CHAT_NOT_FOUND);
        }

        @Test
        void deleteChat_notParticipant_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            ErrorResponse.class,
                            2);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCESS_DENIED);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void deleteChat_shouldSoftDelete() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            ErrorResponse.class,
                            2);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            Chat chat = chatRepository.findById(2L).orElseThrow();
            assertThat(chat.getFirstDeletedByUser().getId()).isEqualTo(1L);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void deleteChat_shouldDeleteChatAndMessages() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/{chatId}",
                            HttpMethod.DELETE,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            Chat chat = chatRepository.findById(1L).orElse(null);
            Message message1 = messageRepository.findById(1L).orElse(null);
            Message message2 = messageRepository.findById(2L).orElse(null);
            assertNull(chat);
            assertNull(message1);
            assertNull(message2);
        }
    }

    // ---------------------------------------------------------
    // existsUnreadMessage()
    // ---------------------------------------------------------

    @Nested
    class ExistsUnreadMessageTests {
        @Test
        void existsUnreadMessage_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/chats/has-unread-message",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void existsUnreadMessage_shouldReturnTrue() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<Boolean> response = template
                    .exchange("/api/chats/has-unread-message",
                            HttpMethod.GET,
                            requestEntity(template),
                            Boolean.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().isTrue();
        }

        @Test
        void existsUnreadMessage_shouldReturnFalse() {
            TestRestTemplate template = tRTemplate(UserEnum.MERRY);

            ResponseEntity<Boolean> response = template
                    .exchange("/api/chats/has-unread-message",
                            HttpMethod.GET,
                            requestEntity(template),
                            Boolean.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().isFalse();
        }
    }

    private void assertChatResponse(ChatResponse chatResponse, Object[] expected) {
        assertThat(chatResponse).isNotNull().extracting(
                        ChatResponse::getId,
                        ChatResponse::getItemId,
                        ChatResponse::getItemTitle,
                        ChatResponse::getItemStatus,
                        c -> c.getPoster().getId(),
                        c -> c.getInterestedUser().getId(),
                        c -> c.getLastMessage().getId(),
                        ChatResponse::getFirstDeletedByUserId)
                .containsExactly(expected);
    }

    private <T> List<T> findChats(UserEnum user, URI uri, Class<T> responseType) {
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
