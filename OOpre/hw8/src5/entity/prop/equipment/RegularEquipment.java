package entity.prop.equipment;

import entity.adventure.Adventurer;

public class RegularEquipment extends Equipment {

    public RegularEquipment(int id, String name, int star, long price) {
        super(id, name, star, price);
    }

    @Override
    public int getHealthPointDecrease(Adventurer adventurer) {
        return super.getStar() * adventurer.getLevel();
    }
}
