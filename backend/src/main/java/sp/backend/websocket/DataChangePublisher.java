package sp.backend.websocket;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DataChangePublisher {

    private final ApplicationEventPublisher eventPublisher;

    public static class Entity {
        public static final String ITEM = "item";
        public static final String CHAT = "chat";
        public static final String MESSAGE = "message";
    }

    public static class Action {
        public static final String CREATE = "create";
        public static final String UPDATE = "update";
        public static final String DELETE = "delete";
        public static final String SOFT_DELETE = "softDelete";
    }

    public DataChangePublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(String entity, String action, Object object) {
        publish(entity, action, object, null);
    }

    public void publish(String entity, String action, Object object, String accountName) {
        eventPublisher.publishEvent(new DataChangeEvent(entity, action, object, accountName));
    }
}