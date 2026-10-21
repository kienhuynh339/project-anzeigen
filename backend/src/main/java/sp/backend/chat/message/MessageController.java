package sp.backend.chat.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class MessageController {
    private final MessageRepository messageRepository;

    private MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{chatId}/messages")
    private ResponseEntity<Page<MessageDto>> findByChatId(@PathVariable Long chatId, Pageable pageable){
        Page<MessageDto> page = messageRepository.findByChatId(chatId, pageable);
        return ResponseEntity.ok(page);
    }

}
