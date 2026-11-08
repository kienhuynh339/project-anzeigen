package sp.backend.websocket;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DataChangePublisher {

    private final ApplicationEventPublisher eventPublisher;

    public DataChangePublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    public void publish(
            String entity,
            String action,
            Long entityId,
            Long parentId
    ) {
        eventPublisher.publishEvent(
                new DataChangedEvent(
                        entity,
                        action,
                        entityId,
                        parentId
                )
        );
    }
}