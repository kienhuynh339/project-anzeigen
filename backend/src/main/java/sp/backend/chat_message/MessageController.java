/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat_message;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.dto.MessageCreateRequest;
import sp.backend.dto.MessageResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    private MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{messageId}")
    private ResponseEntity<MessageResponse> findById(@PathVariable Long messageId, Authentication authentication) {
        MessageResponse messageResponse = messageService.findById(messageId, authentication);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/chat/{chatId}")
    private ResponseEntity<Page<MessageResponse>> findByChatId(@PathVariable Long chatId, Pageable pageable, Authentication authentication) {
        Page<MessageResponse> page = messageService.findByChatId(chatId, authentication, pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/chat/{chatId}")
    private ResponseEntity<Void> markMessagesAsRead(@PathVariable Long chatId, Authentication authentication) {
        messageService.markMessagesAsRead(chatId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    private ResponseEntity<Void> createMessage(@Valid @RequestBody MessageCreateRequest messageCreateRequest, UriComponentsBuilder ucb, Authentication authentication) {
        Message message = messageService.createMessage(messageCreateRequest, authentication);

        URI locationOfNewMessage = ucb.path("/{messageId}").buildAndExpand(message.getId()).toUri();

        return ResponseEntity.created(locationOfNewMessage).build();
    }

}
