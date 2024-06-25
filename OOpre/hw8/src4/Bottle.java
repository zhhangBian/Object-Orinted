public class Bottle {
    private final  int id;
    private final String name;
    private int capacity;
    private boolean isEmpty;
    private boolean iswith;
    private long price;

    public Bottle(int id,String name,int capacity,long price) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.isEmpty = false;
        this.iswith = false;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }

    public int getCapacity() {
        return  capacity;
    }

    public boolean whetherWith() {
        return iswith;
    }

    public boolean whetherEmpty() {
        return isEmpty;
    }

    public void drunk() {
        isEmpty = true;
    }

    public void with() {
        iswith = true;
    }
}
