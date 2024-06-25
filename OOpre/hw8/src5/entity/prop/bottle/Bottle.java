package entity.prop.bottle;

import constants.AbstractBaseTypeEnum;
import entity.adventure.Adventurer;
import entity.prop.Prop;

import java.util.Map;
import java.util.TreeSet;

public abstract class Bottle extends Prop {

    private final int capacity;

    private boolean isEmpty;

    public Bottle(int id, String name, int capacity, long price) {
        super(id, name, price);
        super.setType(AbstractBaseTypeEnum.BOTTLE);
        this.capacity = capacity;
        this.isEmpty = false;
    }

    @Override
    public void carryBy(Adventurer adventurer) {
        int maxBottles = (adventurer.getLevel() / 5) + 1;
        String name = super.getName();
        Map<String, TreeSet<Prop>> bottleBag = adventurer.getBag().get(AbstractBaseTypeEnum.BOTTLE);
        if (bottleBag.containsKey(name) && bottleBag.get(name).size() >= maxBottles) {
            return;
        }
        if (!bottleBag.containsKey(name)) {
            TreeSet<Prop> newSet = new TreeSet<>(new Prop.PropIdCmp());
            bottleBag.put(name, newSet);
        }
        bottleBag.get(name).add(this);
    }

    @Override
    public void useBy(Adventurer adventurer) {
        if (this.isEmpty) {
            adventurer.delProp(this);
        } else {
            this.isEmpty = true;
            int recoveredHitPoint = adventurer.getHitPoint() + getRecoverHitPoint(adventurer);
            adventurer.setHitPoint(recoveredHitPoint);
        }
    }

    public abstract int getRecoverHitPoint(Adventurer adventurer);

    public int getCapacity() {
        return capacity;
    }

}
