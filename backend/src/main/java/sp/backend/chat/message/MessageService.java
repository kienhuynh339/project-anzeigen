/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    Page<MessageDto> findByChatId(Long chatId, Pageable pageable){
        return messageRepository.findByChatId(chatId, pageable);
    }

    Optional<Message> findFirstByChat_IdOrderByCreatedAtDesc(Long chatId){
        return messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chatId);
    }

    void deleteByChat_Id(Long chatId){
        messageRepository.deleteByChat_Id(chatId);
    }

    @Transactional
    int markMessagesAsReadByChat_IdAndSender_Id(Long chatId, Long senderId){
        return messageRepository.markMessagesAsReadByChat_IdAndSender_Id(chatId, senderId);
    }
}
