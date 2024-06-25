public class Bottle {
    private int id;
    private String name;
    private int capacity;                                    //bottle容量
    private Boolean isEmpty;
    private boolean inBackpack;
    private long price;
    private String type;

    public Bottle(int id,String name,int capacity,long price) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.isEmpty = false;
        this.inBackpack = false;
        this.price = price;
    }

    public boolean isInBackpack() {
        return inBackpack;
    }

    public String getName() {
        return name;
    }

    public void takeOn() {
        inBackpack = true;
    }

    public void takeOff() {
        inBackpack = false;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPoint(int hitPoint) {
        return capacity;
    }

    public void emptyBottle() {
        isEmpty = true;
    }

    public Boolean isEmpty() {
        return isEmpty;
    }

    public long getPrice() {
        return price;
    }

    public String getType() {
        return "Bottle";
    }
}
