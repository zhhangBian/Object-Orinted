public class Food {
    private int foodId;
    private String foodName;
    private int energy;

    public Food(int foodId, String foodName, int energy) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.energy = energy;
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
