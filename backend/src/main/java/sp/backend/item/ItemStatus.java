package sp.backend.item;

public enum ItemStatus {
    DRAFT(1),
    ACTIVE(2),
    RESERVED(3),
    DELETED(4);

    private final int value;

    ItemStatus(int value){this.value = value;}

    public int getValue(){
        return this.value;
    }

}
