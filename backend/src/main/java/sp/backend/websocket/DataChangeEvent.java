/**
 * @author Trung Kien Huynh
 */
package sp.backend.websocket;

public class DataChangeEvent {
    private String entity;
    private String action;
    private Object object;
    private String accountName;

    public DataChangeEvent(String entity, String action, Object object, String accountName) {
        this.entity = entity;
        this.action = action;
        this.object = object;
        this.accountName = accountName;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}