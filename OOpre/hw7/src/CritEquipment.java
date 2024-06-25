public class CritEquipment extends Equipment {
    private int critical;

    public CritEquipment(int id, String name, int star, long price, int critical) {
        super(id, name, star, price);
        this.critical = critical;
    }

    @Override
    public int UseEquipment(int advLevel, int hitPoint) {
        return super.GetStar() * advLevel + critical;
    }
}
