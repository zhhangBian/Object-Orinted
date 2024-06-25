package entity.prop.bottle;

import entity.adventure.Adventurer;

public class RegularBottle extends Bottle {

    public RegularBottle(int id, String name, int capacity, long price) {
        super(id, name, capacity, price);
    }

    @Override
    public int getRecoverHitPoint(Adventurer adventurer) {
        return super.getCapacity();
    }

}
