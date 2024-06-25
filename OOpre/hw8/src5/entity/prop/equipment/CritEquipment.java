package entity.prop.equipment;

import entity.adventure.Adventurer;

public class CritEquipment extends Equipment {

    private final int critical;

    public CritEquipment(int id, String name, int star, long price, int critical) {
        super(id, name, star, price);
        this.critical = critical;
    }

    @Override
    public int getHealthPointDecrease(Adventurer adventurer) {
        return super.getStar() * adventurer.getLevel() + critical;
    }
}
