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
import sp.backend.chat.message.Message;
import sp.backend.chat.message.MessageDto;
import sp.backend.chat.message.MessageRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.utils.Utils.ExceptionMessage;

import static sp.backend.chat.ChatWithUser.checkIfUserIsParticipantOfChat;
import static sp.backend.utils.Utils.getLoggedInUser;
import static sp.backend.utils.Utils.getLoggedInUserId;

import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Page<ChatDto> findByUser(Authentication authentication, Pageable pageable) {
        Long userId = getLoggedInUserId(authentication, userRepository, true);

        Page<ChatDto> chats = chatRepository.findByUserId(userId, pageable);
        // Füge jedem Chat die letzte Nachricht hinzu
        chats.stream().forEach((chat) -> {
            Message lastMessage = messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chat.getId()).orElseThrow(() ->
                    new EntityNotFoundException(ExceptionMessage.MESSAGE_NOT_FOUND.getMessage()));
            chat.setLastMessage(new MessageDto(lastMessage));
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
            return;
        }

        // Chat wurde bereits vom anderen Teilnehmer als gelöscht markiert (durch firstDeletedByUser)
        // -> Chat und zugehörige Nachrichten löschen
        messageRepository.deleteByChat_Id(chatId);
        chatRepository.delete(chat);
    }

}
