package sp.backend.item;

import jakarta.persistence.EnumeratedValue;

public enum ItemStatus {
    DRAFT(1),
    ACTIVE(2),
    RESERVED(3),
    DELETED(4);

    @EnumeratedValue
    private final int value;

    ItemStatus(int value){this.value = value;}
    public int getValue(){
        return this.value;
    }

}
