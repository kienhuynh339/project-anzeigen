package sp.backend.websocket;

public class DataChangeEvent {
    private String entity;
    private String action;
    private Long entityId;
    private Long parentId;

    public DataChangeEvent(String entity, String action, Long entityId, Long parentId) {
        this.entity = entity;
        this.action = action;
        this.entityId = entityId;
        this.parentId = parentId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}