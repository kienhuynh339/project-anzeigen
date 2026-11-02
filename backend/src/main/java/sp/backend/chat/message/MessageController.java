/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    private MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{messageId}")
    private ResponseEntity<MessageDto> findByMessageId(@PathVariable Long messageId, Authentication authentication){
        MessageDto messageDto = messageService.findByMessageId(messageId, authentication);
        return ResponseEntity.ok(messageDto);
    }

    @GetMapping("/chat/{chatId}")
    private ResponseEntity<Page<MessageDto>> findByChatId(@PathVariable Long chatId, Pageable pageable, Authentication authentication){
        Page<MessageDto> page = messageService.findByChatId(chatId,authentication, pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/chat/{chatId}")
    private ResponseEntity<Void> markMessagesAsRead(@PathVariable Long chatId, Authentication authentication){
        messageService.markMessagesAsReadByChat_IdAndSender_Id(chatId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    private ResponseEntity<Void>createMessage(@RequestBody CreateMessageRequest createMessageRequest, UriComponentsBuilder ucb, Authentication authentication){
        Message message = messageService.createMessage(createMessageRequest,authentication);
        URI locationOfNewMessage = ucb.path("/{messageId}").buildAndExpand(message.getId()).toUri();
        return ResponseEntity.created(locationOfNewMessage).build();
    }

}
