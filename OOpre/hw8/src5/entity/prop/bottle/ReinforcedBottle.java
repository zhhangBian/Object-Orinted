package entity.prop.bottle;

import entity.adventure.Adventurer;

public class ReinforcedBottle extends Bottle {

    private final double ratio;

    public ReinforcedBottle(int id, String name, int capacity, long price, double ratio) {
        super(id, name, capacity, price);
        this.ratio = ratio;
    }

    @Override
    public int getRecoverHitPoint(Adventurer adventurer) {
        return (int)((1 + ratio) * super.getCapacity());
    }

}
