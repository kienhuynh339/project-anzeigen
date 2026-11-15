package sp.backend.chat_message;

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

        verifyNoInteractions(chatRepository);
    }

    @Test
    void findById_shouldThrowWhenChatDoesNotExist() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageService.findById(1L, authentication));
        assertEquals(ExceptionMessage.CHAT_NOT_FOUND, exception.getMessage());

        verify(chat, never()).isParticipant(any());
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

    // ---------------------------------------------------------
    // findByChatId()
    // ---------------------------------------------------------

    @Test
    void findByChatId_shouldThrowWhenUserIsNotAuthenticated() {
        Pageable pageable = PageRequest.of(0, 10);

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> messageService.findByChatId(1L, null, pageable));
        assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

        verifyNoInteractions(chatRepository);
    }

    @Test
    void findByChatId_shouldThrowWhenChatDoesNotExist() {
        Pageable pageable = PageRequest.of(0, 10);

        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageService.findByChatId(1L, authentication, pageable));
        assertEquals(ExceptionMessage.CHAT_NOT_FOUND, exception.getMessage());

        verify(chat, never()).isParticipant(any());
    }

    @Test
    void findByChatId_shouldThrowWhenUserIsNotParticipant() {
        Pageable pageable = PageRequest.of(0, 10);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(sender)).thenReturn(false);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> messageService.findByChatId(1L, authentication, pageable));
        assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());
    }

    @Test
    void findByChatId_shouldReturnMessageResponses() {
        Pageable pageable = PageRequest.of(0, 10);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(sender)).thenReturn(true);

        Page<MessageResponse> page = new PageImpl<>(List.of(new MessageResponse(message)));

        when(messageRepository.findByChatId(1L, pageable)).thenReturn(page);

        Page<MessageResponse> result = messageService.findByChatId(1L, authentication, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        MessageResponse messageResponse = result.getContent().get(0);

        assertEquals(1L, messageResponse.getId());
    }

    // ---------------------------------------------------------
    // markMessagesAsRead()
    // ---------------------------------------------------------

    @Test
    void markMessagesAsRead_shouldThrowWhenUserIsNotAuthenticated() {
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> messageService.markMessagesAsRead(1L, null));
        assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

        verifyNoInteractions(chatRepository);
    }

    @Test
    void markMessagesAsRead_shouldThrowWhenChatDoesNotExist() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> messageService.markMessagesAsRead(1L, authentication));
        assertEquals(ExceptionMessage.CHAT_NOT_FOUND, exception.getMessage());

        verify(chat, never()).isParticipant(any());
    }

    @Test
    void markMessagesAsRead_shouldThrowWhenUserIsNotParticipant() {
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(sender)).thenReturn(false);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> messageService.markMessagesAsRead(1L, authentication));
        assertEquals(ExceptionMessage.ACCESS_DENIED, exception.getMessage());

        verify(chat, never()).getOtherParticipant(any());
    }

    @Test
    void markMessagesAsRead_shouldMarkMessagesAsRead() {
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        when(chat.isParticipant(sender)).thenReturn(true);

        User otherParticipant = mock(User.class);
        when(otherParticipant.getId()).thenReturn(2L);

        when(chat.getOtherParticipant(sender)).thenReturn(otherParticipant);

        messageService.markMessagesAsRead(1L, authentication);

        verify(messageRepository).markMessagesAsReadByChat_IdAndSender_Id(1L, 2L);
    }

    // ---------------------------------------------------------
    // createMessage()
    // ---------------------------------------------------------

    @Nested
    class CreateMessageTests {
        @MockitoBean
        private ItemRepository itemRepository;

        private MessageCreateRequest request;
        private Item item;
        private User interestedUser;

        @BeforeEach
        void setUp() {
            request = new MessageCreateRequest();
            request.setItemId(1L);
            request.setInterestedUserId(3L);
            request.setContent("message content");

            item = mock(Item.class);
            interestedUser = mock(User.class);

            when(interestedUser.getId()).thenReturn(3L);

        }

        @Test
        void createMessage_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> messageService.createMessage(request, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verifyNoInteractions(itemRepository);
        }

        @Test
        void createMessage_shouldThrowWhenItemDoesNotExist() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> messageService.createMessage(request, authentication));
            assertEquals(ExceptionMessage.ITEM_NOT_FOUND, exception.getMessage());

            verify(item, never()).getPoster();
        }

        @Test
        void createMessage_shouldThrowWhenInterestedUserDoesNotExist() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

            when(userRepository.findById(request.getInterestedUserId())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> messageService.createMessage(request, authentication));
            assertEquals(ExceptionMessage.USER_NOT_FOUND, exception.getMessage());

            verify(chatRepository, never()).findByItem_IdAndInterestedUser_Id(any(), any());
        }

        @Test
        void createMessage_shouldThrowWhenChatDoesNotExistAndItemIsDeleted() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

            when(userRepository.findById(request.getInterestedUserId())).thenReturn(Optional.of(interestedUser));

            when(chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()))
                    .thenReturn(Optional.empty());

            when(item.getStatus()).thenReturn(ItemStatus.DELETED);

            CannotStartNewChatForDeletedItemException exception = assertThrows(CannotStartNewChatForDeletedItemException.class,
                    () -> messageService.createMessage(request, authentication));
            assertEquals(ExceptionMessage.CANNOT_START_NEW_CHAT_FOR_DELETED_ITEM, exception.getMessage());

            verify(chatRepository, never()).save(any());
        }

        @Test
        void createMessage_shouldThrowWhenCreateNewChatAndItemIsDeleted() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

            when(userRepository.findById(request.getInterestedUserId())).thenReturn(Optional.of(interestedUser));

            when(chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()))
                    .thenReturn(Optional.empty());

            when(item.getStatus()).thenReturn(ItemStatus.DELETED);

            CannotStartNewChatForDeletedItemException exception = assertThrows(CannotStartNewChatForDeletedItemException.class,
                    () -> messageService.createMessage(request, authentication));
            assertEquals(ExceptionMessage.CANNOT_START_NEW_CHAT_FOR_DELETED_ITEM, exception.getMessage());

            verify(chatRepository, never()).save(any());
        }

        @Test
        void createMessage_shouldCreateNewChat() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

            when(userRepository.findById(request.getInterestedUserId())).thenReturn(Optional.of(interestedUser));

            when(chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()))
                    .thenReturn(Optional.empty());

            when(chatRepository.save(any())).thenReturn(chat);

            when(item.getStatus()).thenReturn(ItemStatus.ACTIVE);

            when(chat.getFirstDeletedByUser()).thenReturn(null);

            Message message = messageService.createMessage(request, authentication);

            assertNotNull(message);
            assertEquals(1L, message.getChat().getId());

            verify(chat, never()).setFirstDeletedByUser(null);
            verify(dataChangePublisher).publish(
                    Entity.CHAT,
                    Action.CREATE,
                    chat,
                    sender.getAccountName());
        }

        @Test
        void createMessage_shouldCreateNewMessage() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

            when(userRepository.findById(request.getInterestedUserId())).thenReturn(Optional.of(interestedUser));

            when(chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()))
                    .thenReturn(Optional.of(chat));

            when(chat.getFirstDeletedByUser()).thenReturn(null);

            Message message = messageService.createMessage(request, authentication);

            assertNotNull(message);
            assertEquals(1L, message.getChat().getId());

            verify(dataChangePublisher).publish(
                    Entity.MESSAGE,
                    Action.CREATE,
                    chat,
                    sender.getAccountName());
        }

        @Test
        void createMessage_shouldCreateNewMessageAndSetFirstDeletedByUserToNull() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

            when(userRepository.findById(request.getInterestedUserId())).thenReturn(Optional.of(interestedUser));

            when(chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()))
                    .thenReturn(Optional.of(chat));

            when(chat.getFirstDeletedByUser()).thenReturn(sender);

            Message message = messageService.createMessage(request, authentication);

            assertNotNull(message);
            assertEquals(1L, message.getChat().getId());

            verify(chat).setFirstDeletedByUser(null);
            verify(dataChangePublisher).publish(
                    Entity.MESSAGE,
                    Action.CREATE,
                    chat,
                    sender.getAccountName());
        }

        @Test
        void createMessage_shouldCreateNewMessageAndRetainFirstDeletedByUser() {
            when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));

            when(userRepository.findById(request.getInterestedUserId())).thenReturn(Optional.of(interestedUser));

            when(chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()))
                    .thenReturn(Optional.of(chat));

            when(chat.getFirstDeletedByUser()).thenReturn(interestedUser);

            Message message = messageService.createMessage(request, authentication);

            assertNotNull(message);
            assertEquals(1L, message.getChat().getId());

            verify(chat, never()).setFirstDeletedByUser(null);
            verify(dataChangePublisher).publish(
                    Entity.MESSAGE,
                    Action.CREATE,
                    chat,
                    sender.getAccountName());
        }
    }
}
