package sp.backend.chat_message;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sp.backend.chat.Chat;
import sp.backend.chat.ChatRepository;
import sp.backend.dto.MessageResponse;
import sp.backend.exception.ExceptionMessage;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.websocket.DataChangePublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MessageServiceTest {
    @Autowired
    private MessageService messageService;

    @MockitoBean
    private ChatRepository chatRepository;

    @MockitoBean
    private MessageRepository messageRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private DataChangePublisher dataChangePublisher;

    private Authentication authentication;
    private User sender;
    private Message message;
    private Chat chat;

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken("sender", "123456");

        sender = mock(User.class);
        message = mock(Message.class);
        chat = mock(Chat.class);

        when(userRepository.findByAccountName("sender")).thenReturn(Optional.of(sender));

        when(sender.getId()).thenReturn(1L);
        when(sender.getAccountName()).thenReturn("sender");

        when(message.getId()).thenReturn(1L);
        when(message.getChat()).thenReturn(chat);
        when(message.getSender()).thenReturn(sender);
        when(chat.getId()).thenReturn(1L);
    }

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Test
    void findById_shouldThrowWhenMessageDoesNotExist() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageService.findById(1L, authentication));
        assertEquals(ExceptionMessage.MESSAGE_NOT_FOUND, exception.getMessage());

        verifyNoInteractions(userRepository);
    }

    @Test
    void findById_shouldThrowWhenUserIsNotAuthenticated() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> messageService.findById(1L, null));
        assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());
    }

    @Test
    void findById_shouldThrowWhenChatDoesNotExist() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageService.findById(1L, authentication));
        assertEquals(ExceptionMessage.CHAT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void findById_shouldThrowWhenUserIsNotParticipant() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(sender)).thenReturn(false);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> messageService.findById(1L, authentication));
        assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());
    }

    @Test
    void findById_shouldReturnMessageResponse() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(sender)).thenReturn(true);

        MessageResponse messageResponse = messageService.findById(1L, authentication);

        assertNotNull(messageResponse);
        assertEquals(message.getId(), messageResponse.getId());
        assertEquals(sender.getId(), messageResponse.getSenderId());
    }

    
}
