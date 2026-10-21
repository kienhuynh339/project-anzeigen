package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatRepository chatRepository;
    private ChatController(ChatRepository chatRepository){this.chatRepository=chatRepository;}

    @GetMapping("/{userId}")
    private ResponseEntity<Page<ChatDto>> findByUserId(@PathVariable Long userId, Pageable pageable){
        Page<ChatDto> page = chatRepository.findByUserId(userId, pageable);
        return ResponseEntity.ok(page);
    }
}
