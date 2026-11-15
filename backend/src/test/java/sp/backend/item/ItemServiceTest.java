package sp.backend.item;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sp.backend.chat.Chat;
import sp.backend.chat.ChatRepository;
import sp.backend.chat_message.Message;
import sp.backend.chat_message.MessageRepository;
import sp.backend.dto.MessageCreateRequest;
import sp.backend.dto.MessageResponse;
import sp.backend.exception.CannotStartNewChatForDeletedItemException;
import sp.backend.exception.ExceptionMessage;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
import sp.backend.item.ItemStatus;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Entity;
import sp.backend.websocket.DataChangePublisher.Action;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceTest {
    @Autowired
    ItemService itemService;

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

    
}
