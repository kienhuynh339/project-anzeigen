package sp.backend.websocket;

public class DataChangeEvent {
    private String entity;
    private String action;
    private Object object;

    public DataChangeEvent(String entity, String action, Object object) {
        this.entity = entity;
        this.action = action;
        this.object = object;
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}