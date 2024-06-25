public class Food {
    private int id;
    private String name;
    private int energy;
    private boolean inBackpack;
    private Long price;

    public Food(int id,String name,int energy,Long price) {
        this.id = id;
        this.name = name;
        this.energy = energy;
        this.inBackpack = false;
        this.price = price;
    }

    public int getEnergy() {                                //获取energy
        return energy;
    }

    public boolean isInBackpack() {                         //获取背包状态
        return inBackpack;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void takeOn() {
        inBackpack = true;
    }

    public void takeOff() {
        inBackpack = false;
    }

    public Long getPrice() {
        return price;
    }
}
