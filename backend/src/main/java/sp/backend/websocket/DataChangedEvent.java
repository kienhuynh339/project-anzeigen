package sp.backend.websocket;

public record DataChangedEvent(
        String entity,
        String action,
        Long entityId,
        Long parentId
) {
}