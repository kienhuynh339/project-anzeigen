package sp.backend.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import sp.backend.chat.Chat;
import sp.backend.dto.Event;
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
            String receiver = chat.getInterestedUser().getAccountName().equals(dataChangeEvent.getAccountName()) ?
                    chat.getPoster().getAccountName()
                    : chat.getInterestedUser().getAccountName();
            Long chatId = chat.getId();

            if (dataChangeEvent.getAction().equals(Action.DELETE)) {
                event = new Event(Entity.CHAT, Action.DELETE, chatId);
            } else if (dataChangeEvent.getAction().equals(Action.SOFT_DELETE)) {
                event = new Event(Entity.CHAT, Action.SOFT_DELETE, chatId);
            } else if (dataChangeEvent.getAction().equals(Action.CREATE)) {
                event = new Event(Entity.CHAT, Action.CREATE, chatId);
                sendChatOrMessageChangeEvent(receiver, "/new-message", event);
            }

            sendChatOrMessageChangeEventToBoth(chat, "/chats", event);

        } else if (dataChangeEvent.getEntity().equals(Entity.MESSAGE)) {
            Event event = null;
            Chat chat = (Chat) dataChangeEvent.getObject();
            String receiver = chat.getInterestedUser().getAccountName().equals(dataChangeEvent.getAccountName()) ?
                    chat.getPoster().getAccountName()
                    : chat.getInterestedUser().getAccountName();
            Long chatId = chat.getId();

            if (dataChangeEvent.getAction().equals(Action.CREATE)) {
                event = new Event(Entity.MESSAGE, Action.CREATE, chatId);
                sendChatOrMessageChangeEventToBoth(chat, "/chats/" + chatId, event);
                sendChatOrMessageChangeEventToBoth(chat, "/chats", event); // "/new-message"
                sendChatOrMessageChangeEvent(receiver, "/new-message", event);
            }

        } else if (dataChangeEvent.getEntity().equals(Entity.ITEM)) {
            Event event = null;
            Long itemId = ((Item) dataChangeEvent.getObject()).getId();

//            if (dataChangeEvent.getAction().equals(Action.CREATE)) {
//                event = new Event(Entity.ITEM, Action.CREATE, itemId);} else

            if (dataChangeEvent.getAction().equals(Action.DELETE)) {
                event = new Event(Entity.ITEM, Action.DELETE, itemId);
                sendItemChangeEvent("/topic/items/" + itemId, event);

            } else if (dataChangeEvent.getAction().equals(Action.UPDATE)) {
                event = new Event(Entity.ITEM, Action.UPDATE, itemId);
                sendItemChangeEvent("/topic/items/" + itemId, event);
            }

//            sendItemChangeEvent("/topic/items", event);
        }


    }

    private void sendChatOrMessageChangeEventToBoth(Chat chat, String destination, Event event) {
        sendChatOrMessageChangeEvent(
                chat.getInterestedUser().getAccountName(),
                destination,
                event
        );
        sendChatOrMessageChangeEvent(
                chat.getPoster().getAccountName(),
                destination,
                event
        );
    }

    private void sendChatOrMessageChangeEvent(String accountName, String destination, Event event) {
        messagingTemplate.convertAndSendToUser(
                accountName,
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