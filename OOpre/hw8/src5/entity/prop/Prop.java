package entity.prop;

import constants.AbstractBaseTypeEnum;
import entity.Commodity;
import entity.adventure.Adventurer;

import java.util.Comparator;

public abstract class Prop implements Commodity {

    private final int id;

    private final String name;

    private final long price;

    private AbstractBaseTypeEnum type;

    public Prop(int id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public abstract void useBy(Adventurer adventurer);

    public abstract void carryBy(Adventurer adventurer);

    public static class PropIdCmp implements Comparator<Prop> {
        @Override
        public int compare(Prop o1, Prop o2) {
            return Integer.compare(o1.getId(), o2.getId());
        }
    }

    @Override
    public long getComPrice() {
        return this.getPrice();
    }

    @Override
    public int getComNum() {
        return 1;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public AbstractBaseTypeEnum getType() {
        return type;
    }

    public void setType(AbstractBaseTypeEnum type) {
        this.type = type;
    }

}
