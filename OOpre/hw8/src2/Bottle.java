public class Bottle implements Commodity {
    // 整数、取值范围：0-2147483647
    private final int id;
    // 字符串、保证不会出现空白字符，长度区间: (0,40)
    private final String name;
    // 整数、取值范围：0 - 2147483647
    private final int capacity;
    private boolean isEmpty;
    private final long price;

    public Bottle(int paraId, String paraName, int paraCapacity, long paraPrice) {
        this.id = paraId;
        this.name = paraName;
        this.capacity = paraCapacity;
        this.isEmpty = false;
        this.price = paraPrice;
    }

    @Override
    public long getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean empty) {
        isEmpty = empty;
    }
}
