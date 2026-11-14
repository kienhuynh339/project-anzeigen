package sp.backend.chat_message;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sp.backend.chat.Chat;
import sp.backend.chat.ChatRepository;
import sp.backend.item.Item;
import sp.backend.item.ItemStatus;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.websocket.DataChangePublisher;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken("sender", "123456");

        sender = mock(User.class);

        when(userRepository.findByAccountName("sender")).thenReturn(Optional.of(sender));

        when(sender.getId()).thenReturn(1L);
        when(sender.getAccountName()).thenReturn("sender");
    }

    
}
