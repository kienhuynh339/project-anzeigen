/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sp.backend.chat.Chat;
import sp.backend.chat.ChatRepository;
import sp.backend.item.Item;
import sp.backend.item.ItemRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

import java.util.Optional;

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

    public Page<MessageDto> findByChatId(Long chatId, Pageable pageable){
        return messageRepository.findByChatId(chatId, pageable);
    }

    public Optional<Message> findFirstByChat_IdOrderByCreatedAtDesc(Long chatId){
        return messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chatId);
    }

    public void deleteByChat_Id(Long chatId){
        messageRepository.deleteByChat_Id(chatId);
    }

    @Transactional
    public int markMessagesAsReadByChat_IdAndSender_Id(Long chatId, Long senderId){
        return messageRepository.markMessagesAsReadByChat_IdAndSender_Id(chatId, senderId);
    }

    @Transactional
    public Message createMessage(CreateMessageRequest request){
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(()->new EntityNotFoundException("Item not found: "+request.getItemId()));
        User poster = item.getPoster();
        User interestedUser = userRepository.findById(request.getInterestedUserId()).orElseThrow(()->new EntityNotFoundException("User not found: "+request.getInterestedUserId()));
        User sender;
        if(poster.getId().equals(request.getSenderId())){
            sender = poster;
        }else if(interestedUser.getId().equals(request.getSenderId())){
            sender = interestedUser;
        }else{
            throw new IllegalArgumentException("Invalid senderId");
        }

        // Chat suchen
        Chat chat = chatRepository.findByItem_IdAndInterestedUser_Id(item.getId(), interestedUser.getId()).orElseGet(()->{
            // Chat existiert nicht. Neu erstellen
            Chat newChat = new Chat(item,poster,interestedUser,null);
            return chatRepository.save(newChat);
        });

        // Message erstellen
        Message message = new Message(chat,sender, request.getContent(), false);

        return messageRepository.save(message);
    }

    public MessageDto findByMessageId(Long messageId){
        return messageRepository.findByMessageId(messageId);
    }
}
