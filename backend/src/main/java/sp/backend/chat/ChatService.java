/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sp.backend.chat.message.Message;
import sp.backend.chat.message.MessageDto;
import sp.backend.chat.message.MessageRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

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


    public Page<ChatDto> findByUserId(Long userId, Pageable pageable){
        Page<ChatDto> chats = chatRepository.findByUserId(userId, pageable);
        chats.stream().forEach((chat)->{
            Optional<Message> messageOptional = messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chat.getId());
            if(messageOptional.isPresent()){
                chat.setLastMessage(new MessageDto(messageOptional.get()));
            }
        });
        return chats;
    }

    @Transactional
    public void deleteChat(Long chatId, Long userId){
        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new EntityNotFoundException("Chat not found: "+chatId));
        User firstDeletedByUser = chat.getFirstDeletedByUser();

        if(firstDeletedByUser == null){
            User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found: "+userId));
            chat.setFirstDeletedByUser(user);
            chatRepository.save(chat);
            return;
        }

        messageRepository.deleteByChat_Id(chatId);
        chatRepository.delete(chat);
    }

}
