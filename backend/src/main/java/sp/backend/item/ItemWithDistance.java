package sp.backend.item;

public class ItemWithDistance {
    private Item item;
    private double distance;
    public ItemWithDistance(Item item, double distance){this.item = item; this.distance = distance;}

    public Item getItem() {
        return item;
    }

    public double getDistance() {
        return distance;
    }
}
