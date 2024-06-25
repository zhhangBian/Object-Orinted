public class ReinforcedBottle extends Bottle {
    private double ratio;
    private String type;

    public ReinforcedBottle(int id, String name, int capacity, long price, double ratio) {
        super(id, name, capacity, price);
        this.ratio = ratio;
        this.type = "ReinforcedBottle";
    }

    public int getPoint(int hitPoint) {
        return  (int)Math.floor((1 + ratio) * super.getCapacity());
    }

    public String getType() {
        return type;
    }

    @Override
    public int getCapacity() {
        return super.getCapacity();
    }
}
