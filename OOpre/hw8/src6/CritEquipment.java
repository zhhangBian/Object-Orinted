public class CritEquipment extends Equipment {
    private int critical;

    private String type;

    public CritEquipment(int id,String name,int star,long price,int critical) {
        super(id,name,star,price);
        this.type = "CritEquipment";
        this.critical = critical;
    }

    @Override
    public int getPoint(Adventure adventure,int hitPoint) {
        return super.getPoint(adventure,hitPoint) + critical;
    }

    public String getType() {
        return type;
    }

    @Override
    public int getStar() {
        return super.getStar();
    }
}
