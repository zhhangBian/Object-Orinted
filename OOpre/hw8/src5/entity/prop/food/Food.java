package entity.prop.food;

import constants.AbstractBaseTypeEnum;
import entity.adventure.Adventurer;
import entity.prop.Prop;

import java.util.Map;
import java.util.TreeSet;

public class Food extends Prop {

    private final int energy;

    public Food(int id, String name, int energy, long price) {
        super(id, name, price);
        super.setType(AbstractBaseTypeEnum.FOOD);
        this.energy = energy;
    }

    @Override
    public void carryBy(Adventurer adventurer) {
        String name = super.getName();
        Map<String, TreeSet<Prop>> foodBag = adventurer.getBag().get(AbstractBaseTypeEnum.FOOD);
        if (!foodBag.containsKey(name)) {
            TreeSet<Prop> newSet = new TreeSet<>(new Prop.PropIdCmp());
            foodBag.put(name, newSet);
        }
        foodBag.get(name).add(this);
    }

    @Override
    public void useBy(Adventurer adventurer) {
        int level = adventurer.getLevel() + this.energy;
        adventurer.setLevel(level);
        adventurer.delProp(this);
    }

    public int getEnergy() {
        return energy;
    }
}
