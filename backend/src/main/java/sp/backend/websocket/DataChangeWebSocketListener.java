package sp.backend.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import sp.backend.chat.Chat;
import sp.backend.item.Item;
import sp.backend.websocket.DataChangePublisher.Entity;
import sp.backend.websocket.DataChangePublisher.Action;

@Component
public class DataChangeWebSocketListener {

    private final SimpMessagingTemplate messagingTemplate;

    public DataChangeWebSocketListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    //Mit AFTER_COMMIT wird die Benachrichtigung erst nach erfolgreichem Commit versendet.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDataChanged(DataChangeEvent dataChangeEvent) {
        if (dataChangeEvent.getEntity().equals(Entity.CHAT)) {
            Event event = null;
            Chat chat = (Chat) dataChangeEvent.getObject();
            Long chatId = chat.getId();

            if (dataChangeEvent.getAction().equals(Action.DELETE)) {
                event = new Event(Entity.CHAT, Action.DELETE, chatId);
            } else if (dataChangeEvent.getAction().equals(Action.CREATE)) {
                event = new Event(Entity.CHAT, Action.CREATE, chatId);
            }

            sendChatOrMessageChangeEvent(chat, "/topic/chats", event);

        } else if (dataChangeEvent.getEntity().equals(Entity.MESSAGE)) {
            Event event = null;
            Chat chat = (Chat) dataChangeEvent.getObject();
            Long chatId = chat.getId();

            if (dataChangeEvent.getAction().equals(Action.CREATE)) {
                event = new Event(Entity.MESSAGE, Action.CREATE, chatId);
                sendChatOrMessageChangeEvent(chat, "/topic/chats/" + chatId, event);
                sendChatOrMessageChangeEvent(chat, "/topic/chats", event);
            }

        } else if (dataChangeEvent.getEntity().equals(Entity.ITEM)) {
            Event event = null;
            Long itemId = ((Item) dataChangeEvent.getObject()).getId();

            if (dataChangeEvent.getAction().equals(Action.CREATE)) {
                event = new Event(Entity.ITEM, Action.CREATE, itemId);

            } else if (dataChangeEvent.getAction().equals(Action.DELETE)) {
                event = new Event(Entity.ITEM, Action.DELETE, itemId);
                sendItemChangeEvent("/topic/items/" + itemId, event);

            } else if (dataChangeEvent.getAction().equals(Action.UPDATE)) {
                event = new Event(Entity.ITEM, Action.UPDATE, itemId);
                sendItemChangeEvent("/topic/items/" + itemId, event);
            }

            sendItemChangeEvent("/topic/items", event);
        }


    }

    private void sendChatOrMessageChangeEvent(Chat chat, String destination, Event event) {
        messagingTemplate.convertAndSendToUser(
                chat.getInterestedUser().getAccountName(),
                destination,
                event
        );
        messagingTemplate.convertAndSendToUser(
                chat.getPoster().getAccountName(),
                destination,
                event
        );
    }

    private void sendItemChangeEvent(String destination, Event event) {
        messagingTemplate.convertAndSend(
                destination,
                event
        );
    }
}