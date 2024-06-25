public class RecoverBottle extends Bottle {
    private double ratio;
    private String type;

    public RecoverBottle(int id, String name, int capacity, long price, double ratio) {
        super(id, name, capacity, price);
        this.ratio = ratio;
        this.type = "RecoverBottle";
    }

    @Override
    public int getPoint(int hitPoint) {
        return (int) Math.floor(hitPoint * ratio);
    }

    public String getType() {
        return type;
    }
}
