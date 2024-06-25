public class ReinforcedBottle extends Bottle {
    private double ratio;

    public ReinforcedBottle(int id, String name, int capacity, long price, double ratio) {
        super(id, name, capacity, price);
        this.ratio = ratio;
    }

    @Override
    public int UseBottle(int hitPoint) {
        return (int)((super.UseBottle(hitPoint)) * (1 + ratio));
    }
}
