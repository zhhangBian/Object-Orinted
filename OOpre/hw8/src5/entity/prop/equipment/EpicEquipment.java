package entity.prop.equipment;

import entity.adventure.Adventurer;

public class EpicEquipment extends Equipment {
    private final double ratio;

    public EpicEquipment(int id, String name, int star, long price, double ratio) {
        super(id, name, star, price);
        this.ratio = ratio;
    }

    @Override
    public int getHealthPointDecrease(Adventurer adventurer) {
        return (int)(adventurer.getAttackTarget().getHitPoint() * ratio);
    }

}
