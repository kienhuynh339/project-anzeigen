package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    private ResponseEntity<Page<ChatDto>> findByUserId(@RequestParam Long userId, Pageable pageable){
        Page<ChatDto> page = chatService.findByUserId(userId, pageable);
        return ResponseEntity.ok(page);
    }
}
