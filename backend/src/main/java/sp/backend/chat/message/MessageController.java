/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat.message;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<MessageDto> findByMessageId(@PathVariable Long messageId){
        MessageDto messageDto = messageService.findByMessageId(messageId);
        return ResponseEntity.ok(messageDto);
    }

    @GetMapping("/chat/{chatId}")
    private ResponseEntity<Page<MessageDto>> findByChatId(@PathVariable Long chatId, Pageable pageable){
        Page<MessageDto> page = messageService.findByChatId(chatId, pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/chat/{chatId}/sender/{senderId}")
    private ResponseEntity<Void> markMessagesAsRead(@PathVariable Long chatId, @PathVariable Long senderId){
        messageService.markMessagesAsReadByChat_IdAndSender_Id(chatId, senderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    private ResponseEntity<Void>createMessage(@RequestBody CreateMessageRequest createMessageRequest, UriComponentsBuilder ucb){
        Message message = messageService.createMessage(createMessageRequest);
        URI locationOfNewMessage = ucb.path("/{messageId}").buildAndExpand(message.getId()).toUri();
        return ResponseEntity.created(locationOfNewMessage).build();
    }

}
