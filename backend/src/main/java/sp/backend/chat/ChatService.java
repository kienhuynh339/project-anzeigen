package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sp.backend.chat.message.Message;
import sp.backend.chat.message.MessageDto;
import sp.backend.chat.message.MessageRepository;

import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
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
}
