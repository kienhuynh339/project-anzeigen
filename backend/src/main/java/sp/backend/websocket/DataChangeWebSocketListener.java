/**
 * @author Trung Kien Huynh
 */
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

    // @TransactionalEventListener -> handleDataChange wird aufgerufen, wenn ein DataChangeEvent veröffentlicht wurde und die zugehörige Transaktion erfolgreich abgeschlossen ist.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDataChange(DataChangeEvent dataChangeEvent) {
        switch (dataChangeEvent.getEntity()) {
            case Entity.CHAT -> {
                Event event = null;
                Chat chat = (Chat) dataChangeEvent.getObject();
                String receiver = chat.getOtherParticipantByAccountName(dataChangeEvent.getAccountName()).getAccountName();
                Long chatId = chat.getId();

                switch (dataChangeEvent.getAction()) {
                    case Action.DELETE -> event = new Event(Entity.CHAT, Action.DELETE, chatId);
                    case Action.SOFT_DELETE -> event = new Event(Entity.CHAT, Action.SOFT_DELETE, chatId);
                    case Action.CREATE -> {
                        event = new Event(Entity.CHAT, Action.CREATE, chatId);
                        sendChatOrMessageChangeEvent(receiver, "/new-message", event);
                    }
                }

                sendChatOrMessageChangeEventToBoth(chat, "/chats", event);

            }
            case Entity.MESSAGE -> {
                Event event = null;
                Chat chat = (Chat) dataChangeEvent.getObject();
                String receiver = chat.getOtherParticipantByAccountName(dataChangeEvent.getAccountName()).getAccountName();
                Long chatId = chat.getId();

                if (dataChangeEvent.getAction().equals(Action.CREATE)) {
                    event = new Event(Entity.MESSAGE, Action.CREATE, chatId);
                    sendChatOrMessageChangeEventToBoth(chat, "/chats/" + chatId, event);
                    sendChatOrMessageChangeEventToBoth(chat, "/chats", event);
                    sendChatOrMessageChangeEvent(receiver, "/new-message", event);
                }

            }
            case Entity.ITEM -> {
                Event event = null;
                Long itemId = ((Item) dataChangeEvent.getObject()).getId();

                if (dataChangeEvent.getAction().equals(Action.DELETE)) {
                    event = new Event(Entity.ITEM, Action.DELETE, itemId);
                    sendItemChangeEvent("/topic/items/" + itemId, event);

                } else if (dataChangeEvent.getAction().equals(Action.UPDATE)) {
                    event = new Event(Entity.ITEM, Action.UPDATE, itemId);
                    sendItemChangeEvent("/topic/items/" + itemId, event);
                }
            }
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
        // Nachricht an einen bestimmten User senden
        messagingTemplate.convertAndSendToUser(
                accountName,
                destination,
                event
        );
    }

    private void sendItemChangeEvent(String destination, Event event) {
        // Nachricht an alle Clients, die ein bestimmtes Topic abonniert haben
        messagingTemplate.convertAndSend(
                destination,
                event
        );
    }
}