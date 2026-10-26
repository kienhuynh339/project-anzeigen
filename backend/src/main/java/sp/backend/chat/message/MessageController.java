/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat/{chatId}")
    private ResponseEntity<Page<MessageDto>> findByChatId(@PathVariable Long chatId, Pageable pageable){
        Page<MessageDto> page = messageService.findByChatId(chatId, pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/chat/{chatId}/sender/{senderId}")
    private ResponseEntity<Void> markMessagesAsRead(@PathVariable Long chatId, @PathVariable Long senderId){
        messageService.markMessagesAsReadByChat_IdAndSender_Id(chatId, senderId);
        return ResponseEntity.noContent().build();
    }

}
