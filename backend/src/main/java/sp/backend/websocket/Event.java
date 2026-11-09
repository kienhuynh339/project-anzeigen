package sp.backend.websocket;

public class Event {
    private String entity;
    private String action;
    private Long entityId;

    public Event(String entity, String action, Long entityId) {
        this.entity = entity;
        this.action = action;
        this.entityId = entityId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
