package sp.backend.chat.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "http://localhost:5173")
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
