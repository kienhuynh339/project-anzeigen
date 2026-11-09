package sp.backend.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DataChangeWebSocketListener {

    private final SimpMessagingTemplate messagingTemplate;

    public DataChangeWebSocketListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    //Mit AFTER_COMMIT wird die Benachrichtigung erst nach erfolgreichem Commit versendet.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDataChanged(DataChangeEvent event) {

        String destination;

        if ("MESSAGE".equals(event.getEntity())) {
            destination = "/topic/chats/" + event.getParentId();
        } else if ("ITEM".equals(event.getEntity())) {
            destination = "/topic/items";
        } else if ("CHAT".equals(event.getEntity())) {
            destination = "/topic/chats";
        } else {
            return;
        }

        messagingTemplate.convertAndSend(
                destination,
                event
        );
    }
}