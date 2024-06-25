public class Equipment implements Commodity {
    private int id;
    private String name;
    private int star;
    private long price;

    public Equipment(int id, String name, int star, long price) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.price = price;
    }

    @Override
    public long GetCommodity() {
        return this.price;
    }

    public void AddStar() {
        this.star++;
    }

    public String GetName() {
        return this.name;
    }

    public int GetStar() {
        return this.star;
    }

    public int GetId() {
        return this.id;
    }

    public int UseEquipment(int advLevel, int hitPoint) {
        return this.star * advLevel;
    }
}
