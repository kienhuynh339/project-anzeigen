/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sp.backend.chat.Chat;
import sp.backend.chat.ChatRepository;
import sp.backend.chat.ChatWithUser;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

import static sp.backend.chat.ChatWithUser.checkIfUserIsParticipantOfChat;

import static sp.backend.utils.Utils.getLoggedInUser;

import java.util.Optional;

import sp.backend.utils.Utils.ExceptionMessage;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public MessageService(MessageRepository messageRepository, ItemRepository itemRepository, UserRepository userRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    public MessageDto findByMessageId(Long messageId, Authentication authentication) {
        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.MESSAGE_NOT_FOUND.getMessage()));
        checkIfUserIsParticipantOfChat(message.getChat().getId(), authentication, chatRepository, userRepository);
        return messageRepository.findByMessageId(messageId);
    }

    public Page<MessageDto> findByChatId(Long chatId, Authentication authentication, Pageable pageable) {
        checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        return messageRepository.findByChatId(chatId, pageable);
    }

    public Optional<Message> findFirstByChat_IdOrderByCreatedAtDesc(Long chatId, Authentication authentication) {
        checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        return messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chatId);
    }

    public void deleteByChat_Id(Long chatId, Authentication authentication) {
        ChatWithUser chatWithUser = checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        User loggedInUser = chatWithUser.getUser();
        Chat chat = chatWithUser.getChat();
        // Nachrichten nur löschen, wenn der User nicht der Erste ist, der den Chat gelöscht hat.
        if (loggedInUser.equals(chat.getFirstDeletedByUser())) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED.getMessage());
        }

        messageRepository.deleteByChat_Id(chatId);
    }

    @Transactional
    public int markMessagesAsReadByChat_IdAndSender_Id(Long chatId, Authentication authentication) {
        ChatWithUser chatWithUser = checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        User loggedInUser = chatWithUser.getUser();
        Chat chat = chatWithUser.getChat();

        User otherParticipant = chat.otherParticipant(loggedInUser);

        return messageRepository.markMessagesAsReadByChat_IdAndSender_Id(chatId, otherParticipant.getId());
    }

    @Transactional
    public Message createMessage(CreateMessageRequest request, Authentication authentication) {
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND.getMessage()));

        User poster = item.getPoster();

        User interestedUser = userRepository.findById(request.getInterestedUserId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage()));

        User sender;
        if (poster.getId().equals(request.getSenderId())) {
            sender = poster;
        } else if (interestedUser.getId().equals(request.getSenderId())) {
            sender = interestedUser;
        } else {
            throw new IllegalArgumentException("Invalid senderId");
        }

        // Chat suchen
        Chat chat = chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()).orElseGet(() -> {
            // Chat existiert nicht. Neu erstellen
            Chat newChat = new Chat(item, poster, interestedUser, null);
            return chatRepository.save(newChat);
        });

        // Message erstellen
        Message message = new Message(chat, sender, request.getContent(), false);

        return messageRepository.save(message);
    }
}


