public class EpicEquipment extends Equipment {
    private Double ratio;
    private String type;

    public EpicEquipment(int id,String name,int capacity,long price,double ratio) {
        super(id,name,capacity,price);
        this.ratio = ratio;
        this.type = "EpicEquipment";
    }

    @Override
    public int getPoint(Adventure adventure,int hitPoint) {
        return (int)Math.floor(hitPoint * ratio);
    }

    public String getType() {
        return type;
    }
}
