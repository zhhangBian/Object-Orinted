public class RegularEquipment extends Equipment {
    private String type;

    public RegularEquipment(int id,String name,int capacity,long price) {
        super(id,name,capacity,price);
        this.type = "RegularEquipment";
    }

    public String getType() {
        return type;
    }
}
