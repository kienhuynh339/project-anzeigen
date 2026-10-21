package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    private final ChatRepository chatRepository;
    private ChatController(ChatRepository chatRepository){this.chatRepository=chatRepository;}

    @GetMapping("/{userId}")
    private ResponseEntity<Page<ChatDto>> findByUserId(@PathVariable Long userId, Pageable pageable){
        Page<ChatDto> page = chatRepository.findByUserId(userId, pageable);
        return ResponseEntity.ok(page);
    }
}
