/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sp.backend.dto.ChatResponse;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    private ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @GetMapping("/{chatId}")
    private ResponseEntity<ChatResponse> findById(@PathVariable Long chatId, Authentication authentication) {
        ChatResponse chatResponse = chatService.findById(chatId, authentication);
        return ResponseEntity.ok(chatResponse);
    }

    @GetMapping
    private ResponseEntity<Page<ChatResponse>> findByUser(Authentication authentication, Pageable pageable) {
        Page<ChatResponse> page = chatService.findByUser(authentication, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/has-unread-message")
    private ResponseEntity<Boolean> existsUnreadMessage(Authentication authentication) {
        Boolean result = chatService.existsUnreadMessage(authentication);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{chatId}")
    private ResponseEntity<Void> deleteChat(@PathVariable Long chatId, Authentication authentication) {
        chatService.deleteChat(chatId, authentication);
        return ResponseEntity.noContent().build();
    }

}
