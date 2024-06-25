public class Food implements Commodity {
    private int foodId;
    private String foodName;
    private int energy;
    private long price;

    public Food(int foodId, String foodName, int energy, long price) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.energy = energy;
        this.price = price;
    }

    @Override
    public long GetCommodity() {
        return this.price;
    }

    public int GetEnergy() {
        return this.energy;
    }

    public String GetName() {
        return this.foodName;
    }

    public int GetId() {
        return this.foodId;
    }
}
