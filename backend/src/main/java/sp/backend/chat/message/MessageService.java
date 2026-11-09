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
import sp.backend.dto.MessageCreateRequest;
import sp.backend.dto.MessageResponse;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

import static sp.backend.chat.ChatWithUser.checkIfUserIsParticipantOfChat;

import static sp.backend.utils.Utils.getLoggedInUser;

import java.util.Optional;

import sp.backend.exception.ExceptionMessage;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Action;
import sp.backend.websocket.DataChangePublisher.Entity;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final DataChangePublisher dataChangePublisher;

    public MessageService(MessageRepository messageRepository, ItemRepository itemRepository, UserRepository userRepository, ChatRepository chatRepository, DataChangePublisher dataChangePublisher) {
        this.messageRepository = messageRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.dataChangePublisher = dataChangePublisher;
    }

    public MessageResponse findByMessageId(Long messageId, Authentication authentication) {
        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.MESSAGE_NOT_FOUND));
        checkIfUserIsParticipantOfChat(message.getChat().getId(), authentication, chatRepository, userRepository);
        return new MessageResponse(message);
    }

    public Page<MessageResponse> findByChatId(Long chatId, Authentication authentication, Pageable pageable) {
        checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        return messageRepository.findByChatId(chatId, pageable);
    }

    public Optional<Message> findFirstByChat_IdOrderByCreatedAtDesc(Long chatId, Authentication authentication) {
        checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        return messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chatId);
    }

    @Transactional
    public int markMessagesAsRead(Long chatId, Authentication authentication) {
        ChatWithUser chatWithUser = checkIfUserIsParticipantOfChat(chatId, authentication, chatRepository, userRepository);
        User loggedInUser = chatWithUser.getUser();
        Chat chat = chatWithUser.getChat();

        User otherParticipant = chat.otherParticipant(loggedInUser);

        return messageRepository.markMessagesAsReadByChat_IdAndSender_Id(chatId, otherParticipant.getId());
    }

    @Transactional
    public Message createMessage(MessageCreateRequest request, Authentication authentication) {
        User messageSender = getLoggedInUser(authentication, userRepository, true);

        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ITEM_NOT_FOUND));

        User itemPoster = item.getPoster();
        User interestedUser = userRepository.findById(request.getInterestedUserId()).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND));

        Optional<Chat> existingChat = chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId());

        boolean createChat = existingChat.isEmpty();

        Chat chat = existingChat.orElseGet(() -> {
            Chat newChat = new Chat(item, itemPoster, interestedUser, null);
            return chatRepository.save(newChat);
        });

        Message message = new Message(chat, messageSender, request.getContent(), false);

        messageRepository.save(message);

        if (createChat) {
            dataChangePublisher.publish(Entity.CHAT, Action.CREATE, chat);
        } else {
            dataChangePublisher.publish(Entity.MESSAGE, Action.CREATE, chat);
        }

        return message;
    }
}


