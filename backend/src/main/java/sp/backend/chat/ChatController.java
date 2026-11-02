/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    private ResponseEntity<Page<ChatDto>> findByUser(Authentication authentication, Pageable pageable) {
        Page<ChatDto> page = chatService.findByUser(authentication, pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{chatId}")
    private ResponseEntity<Void> deleteChat(@PathVariable Long chatId, Authentication authentication) {
        chatService.deleteChat(chatId, authentication);
        return ResponseEntity.noContent().build();
    }

}
