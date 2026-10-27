package sp.backend.item;

public class UpdateItemStatusRequest {
    private ItemStatus status;
    public UpdateItemStatusRequest() {}
    public UpdateItemStatusRequest(ItemStatus status) {
        this.status = status;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
