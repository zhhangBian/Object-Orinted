public class Bottle implements Commodity {
    private int id;
    private String name;
    private int capacity;
    private long price;
    private boolean isEmpty;

    public Bottle(int id, String name, int capacity, long price) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.isEmpty = false;
        this.price = price;
    }

    @Override
    public long GetCommodity() {
        return this.price;
    }

    public String GetName() {
        return this.name;
    }

    public int GetId() {
        return this.id;
    }

    public int UseBottle(int hitPoint) {
        this.isEmpty = true;
        return this.capacity;
    }

    public boolean GetIsEmpty() {
        return this.isEmpty;
    }

    public void SetEmpty() {
        this.isEmpty = true;
    }
}