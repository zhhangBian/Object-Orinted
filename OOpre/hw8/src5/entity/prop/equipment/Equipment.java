package entity.prop.equipment;

import constants.AbstractBaseTypeEnum;
import entity.adventure.Adventurer;
import entity.prop.Prop;

import java.util.Map;
import java.util.TreeSet;

public abstract class Equipment extends Prop {

    private int star;

    public Equipment(int id, String name, int star, long price) {
        super(id, name, price);
        super.setType(AbstractBaseTypeEnum.EQUIPMENT);
        this.star = star;
    }

    @Override
    public void carryBy(Adventurer adventurer) {
        Map<String, TreeSet<Prop>> equipmentBag = adventurer.getBag().
                                                    get(AbstractBaseTypeEnum.EQUIPMENT);
        if (!equipmentBag.containsKey(super.getName())) {
            TreeSet<Prop> newSet = new TreeSet<>(new Prop.PropIdCmp());
            newSet.add(this);
            equipmentBag.put(super.getName(), newSet);
        } else {
            TreeSet<Prop> set = equipmentBag.get(super.getName());
            if (!set.contains(this)) {
                for (Prop removeEquipment : set) {
                    set.remove(removeEquipment);
                }
                set.add(this);
            }
        }
    }

    @Override
    public void useBy(Adventurer adventurer) {
        Adventurer attacked = adventurer.getAttackTarget();
        int healthPointDecrease =  getHealthPointDecrease(adventurer);
        attacked.attacked(healthPointDecrease);
    }

    public abstract int getHealthPointDecrease(Adventurer adventurer);

    public void equStarPlus() {
        this.star += 1;
    }

    public int getStar() {
        return star;
    }

}
