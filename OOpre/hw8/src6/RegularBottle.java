public class RegularBottle extends Bottle {
    private String type;

    public RegularBottle(int id, String name, int capacity, long price) {
        super(id,name,capacity,price);
        this.type = "RegularBottle";
    }

    public String getType() {
        return type;
    }
}
