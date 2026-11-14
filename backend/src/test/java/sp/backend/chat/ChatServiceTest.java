package sp.backend.chat;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import sp.backend.chat_message.Message;
import sp.backend.chat_message.MessageRepository;
import sp.backend.dto.ChatResponse;
import sp.backend.dto.MessageResponse;
import sp.backend.exception.ExceptionMessage;
import sp.backend.item.Item;
import sp.backend.item.ItemStatus;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Entity;
import sp.backend.websocket.DataChangePublisher.Action;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @MockitoBean
    private ChatRepository chatRepository;

    @MockitoBean
    private MessageRepository messageRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private DataChangePublisher dataChangePublisher;

    private Authentication authentication;
    private User poster;
    private User interestedUser;
    private Chat chat;
    private Message lastMessage;

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken("poster", "123456");

        // Mockito.mock() erzeugt ein Mock-Objekt
        // das Mock-Objekt kann die Methoden des echten Objekts aufrufen
        // diese liefern aber keine echten Werte zurück (null bei Referenzdatentypen und Standardwert bei Primitivdatentypen)
        // dem Mock-Objekt Werte geben mit when().thenReturn()
        poster = mock(User.class);
        interestedUser = mock(User.class);
        chat = mock(Chat.class);
        lastMessage = mock(Message.class);
        Item item = mock(Item.class);

        when(userRepository.findByAccountName("poster")).thenReturn(Optional.of(poster));

        // User
        when(poster.getId()).thenReturn(1L);
        when(poster.getAccountName()).thenReturn("poster");

        when(interestedUser.getId()).thenReturn(2L);
        when(interestedUser.getAccountName()).thenReturn("interestedUser");

        // Item
        when(item.getId()).thenReturn(1L);
        when(item.getTitle()).thenReturn("test item");
        when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

        // Chat
        when(chat.getId()).thenReturn(1L);
        when(chat.getItem()).thenReturn(item);
        when(chat.getPoster()).thenReturn(poster);
        when(chat.getInterestedUser()).thenReturn(interestedUser);
        when(chat.getFirstDeletedByUser()).thenReturn(null);

        // Message
        when(lastMessage.getId()).thenReturn(1L);
        when(lastMessage.getSender()).thenReturn(interestedUser);
        when(lastMessage.getContent()).thenReturn("test message");
        when(lastMessage.isRead()).thenReturn(false);
        when(lastMessage.getCreatedAt()).thenReturn(OffsetDateTime.now());
    }

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Test
    void findById_shouldReturnChatResponse() {
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(poster)).thenReturn(true);

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(lastMessage));

        ChatResponse chatResponse = chatService.findById(1L, authentication);

        MessageResponse messageResponse = chatResponse.getLastMessage();

        assertNotNull(chatResponse);

        assertEquals(1L, chatResponse.getId());
        assertEquals(1L, chatResponse.getItemId());
        assertEquals("test item", chatResponse.getItemTitle());
        assertEquals(ItemStatus.ACTIVE, chatResponse.getItemStatus());
        assertEquals(poster.getId(), chatResponse.getPoster().getId());
        assertEquals(interestedUser.getId(), chatResponse.getInterestedUser().getId());
        assertEquals(1L, messageResponse.getId());
    }

    @Test
    void findById_shouldThrowWhenUserIsNotAuthenticated() {
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> chatService.findById(1L, null));
        assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

        // Während des Tests wurde keine einzige Methode von chatRepository oder messageRepository aufgerufen
        verifyNoInteractions(chatRepository, messageRepository);
    }

    @Test
    void findById_shouldThrowWhenChatDoesNotExist() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> chatService.findById(1L, authentication));
        assertEquals(ExceptionMessage.CHAT_NOT_FOUND, exception.getMessage());

        // verify(mock, Anzahl(optional, default 1)) prüft, ob eine Methode aufgerufen wurde.
        // findFirstByChat_IdOrderByCreatedAtDesc(1L) prüft nur, dass die Methode nicht mit 1L aufgerufen wurde.
        // anyLong() -> prüft, dass die Methode mit keinem Long-Parameter aufgerufen wurde
        verify(messageRepository, never()).findFirstByChat_IdOrderByCreatedAtDesc(anyLong());
    }

    @Test
    void findById_shouldThrowWhenUserIsNotParticipant() {
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(poster)).thenReturn(false);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> chatService.findById(1L, authentication));
        assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());

        verifyNoInteractions(messageRepository);
    }

    @Test
    void findById_shouldThrowWhenLastMessageDoesNotExist() {
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(poster)).thenReturn(true);

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> chatService.findById(1L, authentication));
        assertEquals(ExceptionMessage.MESSAGE_NOT_FOUND, exception.getMessage());
    }


    // ---------------------------------------------------------
    // findByUser()
    // ---------------------------------------------------------

    @Test
    void findByUser_shouldReturnChatsWithLastMessages() {
        Chat chat1 = mock(Chat.class);
        Chat chat2 = mock(Chat.class);

        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(chat1.getId()).thenReturn(1L);
        when(chat1.getItem()).thenReturn(item1);
        when(chat1.getPoster()).thenReturn(poster);
        when(chat1.getInterestedUser()).thenReturn(interestedUser);
        when(chat1.getFirstDeletedByUser()).thenReturn(null);

        when(chat2.getId()).thenReturn(2L);
        when(chat2.getItem()).thenReturn(item2);
        when(chat2.getPoster()).thenReturn(poster);
        when(chat2.getInterestedUser()).thenReturn(interestedUser);
        when(chat2.getFirstDeletedByUser()).thenReturn(interestedUser);

        when(item1.getId()).thenReturn(1L);
        when(item1.getTitle()).thenReturn("Item 1");
        when(item1.getStatus()).thenReturn(ItemStatus.ACTIVE);

        when(item2.getId()).thenReturn(2L);
        when(item2.getTitle()).thenReturn("Item 2");
        when(item2.getStatus()).thenReturn(ItemStatus.ACTIVE);

        Message lastMessage1 = mock(Message.class);
        Message lastMessage2 = mock(Message.class);

        when(lastMessage1.getId()).thenReturn(1L);
        when(lastMessage1.getSender()).thenReturn(interestedUser);
        when(lastMessage2.getId()).thenReturn(2L);
        when(lastMessage2.getSender()).thenReturn(interestedUser);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<ChatResponse> page = new PageImpl<>(
                List.of(new ChatResponse(chat1, null),
                        new ChatResponse(chat2, null)));

        when(chatRepository.findByUserId(1L, pageable)).thenReturn(page);

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L)).thenReturn(Optional.of(lastMessage1));
        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(2L)).thenReturn(Optional.of(lastMessage2));

        Page<ChatResponse> result = chatService.findByUser(authentication, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        ChatResponse chatResponse1 = result.getContent().get(0);
        ChatResponse chatResponse2 = result.getContent().get(1);

        assertEquals(1L, chatResponse1.getId());
        assertEquals(1L, chatResponse1.getItemId());
        assertEquals("Item 1", chatResponse1.getItemTitle());
        assertEquals(ItemStatus.ACTIVE, chatResponse1.getItemStatus());
        assertEquals(poster.getId(), chatResponse1.getPoster().getId());
        assertEquals(interestedUser.getId(), chatResponse1.getInterestedUser().getId());
        assertEquals(lastMessage1.getId(), chatResponse1.getLastMessage().getId());
        assertNull(chatResponse1.getFirstDeletedByUserId());

        assertEquals(2L, chatResponse2.getId());
        assertEquals(2L, chatResponse2.getItemId());
        assertEquals("Item 2", chatResponse2.getItemTitle());
        assertEquals(ItemStatus.ACTIVE, chatResponse2.getItemStatus());
        assertEquals(poster.getId(), chatResponse2.getPoster().getId());
        assertEquals(interestedUser.getId(), chatResponse2.getInterestedUser().getId());
        assertEquals(lastMessage2.getId(), chatResponse2.getLastMessage().getId());
        assertEquals(interestedUser.getId(), chatResponse2.getFirstDeletedByUserId());
    }

    @Test
    void findByUser_shouldReturnEmptyPage() {
        PageRequest pageable = PageRequest.of(0, 10);

        when(chatRepository.findByUserId(1L, pageable)).thenReturn(Page.empty());

        Page<ChatResponse> result = chatService.findByUser(authentication, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verifyNoInteractions(messageRepository);
    }

    // #++
    @Test
    void findByUser_shouldThrowWhenLastMessageDoesNotExist() {
        PageRequest pageable = PageRequest.of(0, 10);

        ChatResponse chatResponse = mock(ChatResponse.class);
        when(chatResponse.getId()).thenReturn(1L);

        Page<ChatResponse> page = new PageImpl<>(
                List.of(chatResponse)//52
        );

        when(chatRepository.findByUserId(1L, pageable))
                .thenReturn(page);

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> chatService.findByUser(authentication, pageable));
    }

    @Test
    void findByUser_shouldThrowWhenUserIsNotAuthenticated() {
        PageRequest pageable = PageRequest.of(0, 10);

        assertThrows(AuthenticationException.class,
                () -> chatService.findByUser(null, pageable));

        verifyNoInteractions(chatRepository);
    }

    @Test
    void findByUser_shouldThrowWhenUserDoesNotExist() {
        PageRequest pageable = PageRequest.of(0, 10);

        when(userRepository.findByAccountName("poster"))
                .thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class,
                () -> chatService.findByUser(authentication, pageable));

        verifyNoInteractions(chatRepository);
    }

    // ---------------------------------------------------------
    // deleteChat()
    // ---------------------------------------------------------

    @Test
    void deleteChat_shouldSoftDeleteWhenNotDeletedBefore() {
        when(chatRepository.findById(1L))
                .thenReturn(Optional.of(chat));

        when(chat.isParticipant(poster)).thenReturn(true);

        when(chat.getFirstDeletedByUser()).thenReturn(null);

        chatService.deleteChat(1L, authentication);

        verify(chat).setFirstDeletedByUser(poster);
        verify(chatRepository).save(chat);

        verify(dataChangePublisher).publish(
                Entity.CHAT,
                Action.SOFT_DELETE,
                chat,
                "poster"
        );

        verify(chatRepository, never()).delete(any());
        verify(messageRepository, never()).deleteByChat_Id(anyLong());
    }

    @Test
    void deleteChat_shouldPermanentlyDeleteWhenAlreadyDeleted() {
        when(chatRepository.findById(1L))
                .thenReturn(Optional.of(chat));

        when(chat.isParticipant(poster)).thenReturn(true);

        when(chat.getFirstDeletedByUser()).thenReturn(interestedUser);

        chatService.deleteChat(1L, authentication);

        verify(messageRepository).deleteByChat_Id(1L);
        verify(chatRepository).delete(chat);

        verify(dataChangePublisher).publish(
                Entity.CHAT,
                Action.DELETE,
                chat,
                "poster"
        );

        verify(chatRepository, never()).save(any());
    }

    @Test
    void deleteChat_shouldThrowWhenChatDoesNotExist() {
        when(chatRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> chatService.deleteChat(1L, authentication));

        verifyNoInteractions(dataChangePublisher);
        verify(messageRepository, never()).deleteByChat_Id(anyLong());
    }

    @Test
    void deleteChat_shouldThrowWhenUserIsNotParticipant() {
        when(chatRepository.findById(1L))
                .thenReturn(Optional.of(chat));

        when(chat.isParticipant(poster)).thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> chatService.deleteChat(1L, authentication));

        verify(chatRepository, never()).save(any());
        verify(chatRepository, never()).delete(any());
        verifyNoInteractions(dataChangePublisher);
    }

    @Test
    void deleteChat_shouldThrowWhenUserIsNotAuthenticated() {
        assertThrows(AuthenticationException.class,
                () -> chatService.deleteChat(1L, null));

        verifyNoInteractions(chatRepository);
        verifyNoInteractions(dataChangePublisher);
    }

    // ---------------------------------------------------------
    // existsUnreadMessage()
    // ---------------------------------------------------------

    @Test
    void existsUnreadMessage_shouldReturnTrueWhenUnreadMessageExists() {
        when(chatRepository.findIdsByUserId(1L))
                .thenReturn(List.of(1L));

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(lastMessage));

        when(lastMessage.getSender()).thenReturn(interestedUser);
        when(lastMessage.isRead()).thenReturn(false);

        assertTrue(chatService.existsUnreadMessage(authentication));

        verify(chatRepository).findIdsByUserId(1L);
    }

    @Test
    void existsUnreadMessage_shouldReturnFalseWhenMessageIsRead() {
        when(chatRepository.findIdsByUserId(1L))
                .thenReturn(List.of(1L));

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(lastMessage));

        when(lastMessage.getSender()).thenReturn(interestedUser);
        when(lastMessage.isRead()).thenReturn(true);

        assertFalse(chatService.existsUnreadMessage(authentication));
    }

    @Test
    void existsUnreadMessage_shouldReturnFalseWhenLastMessageWasSentByLoggedInUser() {
        when(chatRepository.findIdsByUserId(1L))
                .thenReturn(List.of(1L));

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(lastMessage));

        when(lastMessage.getSender()).thenReturn(poster);

        assertFalse(chatService.existsUnreadMessage(authentication));

        verify(lastMessage, never()).isRead();
    }

    @Test
    void existsUnreadMessage_shouldReturnFalseWhenNoMessageExists() {
        when(chatRepository.findIdsByUserId(1L))
                .thenReturn(List.of(1L));

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.empty());

        assertFalse(chatService.existsUnreadMessage(authentication));
    }

    @Test
    void existsUnreadMessage_shouldReturnFalseWhenNoChatsExist() {
        when(chatRepository.findIdsByUserId(1L))
                .thenReturn(List.of());

        assertFalse(chatService.existsUnreadMessage(authentication));

        verifyNoInteractions(messageRepository);
    }

    @Test
    void existsUnreadMessage_shouldThrowWhenUserIsNotAuthenticated() {
        assertThrows(AuthenticationException.class,
                () -> chatService.existsUnreadMessage(null));

        verifyNoInteractions(chatRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    void existsUnreadMessage_shouldReturnTrueWhenAnyChatHasUnreadMessage() {
        when(chatRepository.findIdsByUserId(1L))
                .thenReturn(List.of(1L, 2L));

        Message readMessage = mock(Message.class);
        Message unreadMessage = mock(Message.class);

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(readMessage));

        when(readMessage.getSender()).thenReturn(interestedUser);
        when(readMessage.isRead()).thenReturn(true);

        when(messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(2L))
                .thenReturn(Optional.of(unreadMessage));

        when(unreadMessage.getSender()).thenReturn(interestedUser);
        when(unreadMessage.isRead()).thenReturn(false);

        assertTrue(chatService.existsUnreadMessage(authentication));
    }
}

