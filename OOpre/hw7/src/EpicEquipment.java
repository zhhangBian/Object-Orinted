public class EpicEquipment extends Equipment {
    private double ratio;

    public EpicEquipment(int id, String name, int star, long price, double ratio) {
        super(id, name, star, price);
        this.ratio = ratio;
    }

    @Override
    public int UseEquipment(int advLevel, int hitPoint) {
        return (int) (hitPoint * this.ratio);
    }
}
