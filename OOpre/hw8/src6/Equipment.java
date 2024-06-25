public class Equipment {
    private int id;
    private String name;
    private int star;//equipment等级
    private boolean inBackpack;
    private long price;

    public Equipment(int id, String name, int star,long price) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.inBackpack = false;
        this.price = price;
    }

    public void addStar() {
        star++;
    }

    public String getName() {
        return name;
    }

    public int getStar() {
        return star;
    }

    public boolean isInBackpack() {
        return inBackpack;
    }

    public void takeOn() {
        inBackpack = true;
    }

    public void takeOff() {
        inBackpack = false;
    }

    public int getPoint(Adventure adventure,int hitPoint) {
        return star * adventure.getLevel();
    }

    public long getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return "Equipment";
    }
}