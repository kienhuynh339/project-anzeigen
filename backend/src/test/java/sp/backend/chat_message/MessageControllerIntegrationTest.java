/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat_message;


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
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.TestUtils;
import sp.backend.chat.Chat;
import sp.backend.chat.ChatRepository;
import sp.backend.dto.*;
import sp.backend.exception.ExceptionMessage;
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
public class MessageControllerIntegrationTest {
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
                    .exchange("/api/messages/{messageId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void findById_messageDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/{messageId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            16);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.MESSAGE_NOT_FOUND);
        }

        @Test
        void findById_notParticipant_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/{messageId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCESS_DENIED);
        }

        @Test
        void findById_shouldReturnMessageResponse() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<MessageResponse> response = template
                    .exchange("/api/messages/{messageId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            MessageResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            Object[] expected = {1L, 2L, "Message 1", true, OffsetDateTime.parse("2026-10-09T18:00:00+02").toInstant()};
            assertMessageResponse(response.getBody(), expected);
        }
    }


    // ---------------------------------------------------------
    // findByChatId()
    // ---------------------------------------------------------

    @Nested
    class FindByChatIdTests {
        @Test
        void findByChatId_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/chat/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void findByChatId_chatDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/chat/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            6);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.CHAT_NOT_FOUND);
        }

        @Test
        void findByChatId_notParticipant_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/chat/{chatId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCESS_DENIED);
        }

        @Test
        void findByChatId_shouldReturnMessageResponses() {
            URI uri = UriComponentsBuilder.fromPath("/api/messages/chat/1").queryParam("page", 0).queryParam("size", 10).queryParam("sort", "id,asc").build().encode().toUri();

            List<MessageResponse> messages = findMessages(UserEnum.SAMWISE, uri, MessageResponse.class);

            assertNotNull(messages);
            assertEquals(2, messages.size());
            assertEquals(1L, messages.get(0).getId());
            assertEquals(2L, messages.get(1).getId());
        }
    }

    // ---------------------------------------------------------
    // markMessagesAsRead()
    // ---------------------------------------------------------

    @Nested
    class MarkMessagesAsReadTests {
        @Test
        void markMessagesAsRead_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/chat/{chatId}",
                            HttpMethod.PATCH,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void markMessagesAsRead_chatDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/chat/{chatId}",
                            HttpMethod.PATCH,
                            requestEntity(template),
                            ErrorResponse.class,
                            6);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.CHAT_NOT_FOUND);
        }

        @Test
        void markMessagesAsRead_notParticipant_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages/chat/{chatId}",
                            HttpMethod.PATCH,
                            requestEntity(template),
                            ErrorResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCESS_DENIED);
        }

        @Test
        void markMessagesAsRead_shouldMarkMessagesAsRead() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<Void> response = template
                    .exchange("/api/messages/chat/{chatId}",
                            HttpMethod.PATCH,
                            requestEntity(template),
                            Void.class,
                            3);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            Message message11 = messageRepository.findById(11L).orElseThrow();
            Message message12 = messageRepository.findById(12L).orElseThrow();
            assertTrue(message11.getRead());
            assertTrue(message12.getRead());
        }
    }

    // ---------------------------------------------------------
    // createMessage()
    // ---------------------------------------------------------

    @Nested
    class CreateMessageTests {
        private MessageCreateRequest requestBody;

        @BeforeEach
        void setUp() {
            requestBody = new MessageCreateRequest();
            requestBody.setItemId(6L);
            requestBody.setInterestedUserId(4L);
            requestBody.setContent("Message 16");
        }

        @Test
        void createMessage_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void createMessage_itemDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setItemId(99L);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ITEM_NOT_FOUND);
        }

        @Test
        void createMessage_interestedUserNotFound_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setInterestedUserId(99L);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_FOUND);
        }

        @Test
        void createMessage_itemIsDeleted_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setItemId(7L);

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/messages",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.CANNOT_START_NEW_CHAT_FOR_DELETED_ITEM);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void createMessage_shouldCreateChatAndMessage() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setItemId(6L);
            requestBody.setInterestedUserId(4L);

            ResponseEntity<Void> response = template
                    .exchange("/api/messages",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).isNotNull().hasPath("/api/messages/16");
            Message message16 = messageRepository.findById(16L).orElseThrow();
            assertThat(message16).extracting(
                            Message::getId,
                            m -> m.getSender().getId(),
                            Message::getContent,
                            Message::getRead,
                            m -> m.getChat().getId())
                    .containsExactly(16L, 4L, "Message 16", false, 6L);
            Chat chat6 = chatRepository.findById(6L).orElseThrow();
            assertThat(chat6).extracting(
                            Chat::getId,
                            c -> c.getItem().getId(),
                            c -> c.getPoster().getId(),
                            c -> c.getInterestedUser().getId(),
                            Chat::getFirstDeletedByUser)
                    .containsExactly(6L, 6L, 3L, 4L, null);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void createMessage_shouldCreateMessageForExistingChat() {
            TestRestTemplate template = tRTemplate(UserEnum.FRODO);

            requestBody.setItemId(2L);
            requestBody.setInterestedUserId(4L);

            ResponseEntity<Void> response = template
                    .exchange("/api/messages",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).isNotNull().hasPath("/api/messages/16");
            Message message16 = messageRepository.findById(16L).orElseThrow();
            assertThat(message16).extracting(
                            Message::getId,
                            m -> m.getSender().getId(),
                            Message::getContent,
                            Message::getRead,
                            m -> m.getChat().getId())
                    .containsExactly(16L, 4L, "Message 16", false, 3L);
            // keinen neuen Chat erstellen
            Chat chat6 = chatRepository.findById(6L).orElse(null);
            assertNull(chat6);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void createMessage_shouldCreateMessageForExistingChat_andResetFirstDeletedByUser() {
            TestRestTemplate template = tRTemplate(UserEnum.MERRY);

            requestBody.setItemId(1L);
            requestBody.setInterestedUserId(2L);

            ResponseEntity<Void> response = template
                    .exchange("/api/messages",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).isNotNull().hasPath("/api/messages/16");
            Message message16 = messageRepository.findById(16L).orElseThrow();
            assertThat(message16).extracting(
                            Message::getId,
                            m -> m.getSender().getId(),
                            Message::getContent,
                            Message::getRead,
                            m -> m.getChat().getId())
                    .containsExactly(16L, 2L, "Message 16", false, 1L);
            Chat chat1 = chatRepository.findById(1L).orElseThrow();
            assertThat(chat1).extracting(
                            Chat::getId,
                            c -> c.getItem().getId(),
                            c -> c.getPoster().getId(),
                            c -> c.getInterestedUser().getId(),
                            Chat::getFirstDeletedByUser)
                    .containsExactly(1L, 1L, 1L, 2L, null);
        }

    }


    private void assertMessageResponse(MessageResponse messageResponse, Object[] expected) {
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
