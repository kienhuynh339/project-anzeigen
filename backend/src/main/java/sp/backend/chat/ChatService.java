/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sp.backend.chat_message.Message;
import sp.backend.dto.ChatResponse;
import sp.backend.dto.MessageResponse;
import sp.backend.chat_message.MessageRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.exception.ExceptionMessage;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Entity;
import sp.backend.websocket.DataChangePublisher.Action;

import java.util.List;

import static sp.backend.chat.ChatWithUser.checkIfUserIsParticipantOfChat;
import static sp.backend.utils.Utils.getLoggedInUser;
import static sp.backend.utils.Utils.getLoggedInUserId;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DataChangePublisher dataChangePublisher;

    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, UserRepository userRepository, DataChangePublisher dataChangePublisher) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.dataChangePublisher = dataChangePublisher;
    }

    public ChatResponse findById(Long chatId, Authentication authentication) {
        ChatWithUser chatWithUser = checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);

        Chat chat = chatWithUser.getChat();

        Message lastMessage = messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chat.getId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.MESSAGE_NOT_FOUND));

        return new ChatResponse(chat, new MessageResponse(lastMessage));
    }

    public Page<ChatResponse> findByUser(Authentication authentication, Pageable pageable) {
        Long userId = getLoggedInUserId(authentication, userRepository, true);

        Page<ChatResponse> chats = chatRepository.findByUserId(userId, pageable);
        // Füge jedem Chat die letzte Nachricht hinzu
        chats.stream().forEach((chat) -> {
            Message lastMessage = messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chat.getId()).orElseThrow(() ->
                    new EntityNotFoundException(ExceptionMessage.MESSAGE_NOT_FOUND));
            chat.setLastMessage(new MessageResponse(lastMessage));
        });
        return chats;
    }

    @Transactional
    public void deleteChat(Long chatId, Authentication authentication) {
        ChatWithUser chatWithUser = checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        Chat chat = chatWithUser.getChat();

        User loggedInUser = chatWithUser.getUser();
        User firstDeletedByUser = chat.getFirstDeletedByUser();

        // Setze Chat.firstDeletedByUser auf loggedInUser, wenn firstDeletedByUser null ist
        if (firstDeletedByUser == null) {
            chat.setFirstDeletedByUser(loggedInUser);
            chatRepository.save(chat);
            dataChangePublisher.publish(Entity.CHAT, Action.SOFT_DELETE, chat, loggedInUser.getAccountName());
        } else {
            // Chat wurde bereits vom anderen Teilnehmer als gelöscht markiert (durch firstDeletedByUser)
            // -> Chat und zugehörige Nachrichten löschen
            messageRepository.deleteByChat_Id(chatId);
            chatRepository.delete(chat);
            dataChangePublisher.publish(Entity.CHAT, Action.DELETE, chat, loggedInUser.getAccountName());
        }
    }

    public boolean existsUnreadMessage(Authentication authentication) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        List<Long> chatIds = chatRepository.findIdsByUserId(loggedInUser.getId());

        return chatIds.stream().anyMatch((chatId) -> {
            Message message = messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chatId).orElse(null);
            if (message != null && !message.getSender().equals(loggedInUser)) {
                return !message.isRead();
            }
            return false;
        });
    }
}
