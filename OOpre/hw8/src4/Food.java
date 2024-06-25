public class Food {

    private final int id;
    private final String name;
    private final int energy;
    private boolean iswith;
    private long price;

    public Food(int id,String name,int energy,long price) {
        this.id = id;
        this.name = name;
        this.energy = energy;
        this.price = price;
    }

    public  String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean whetherWith() {
        return iswith;
    }

    public int getEnergy() {
        return energy;
    }

    public void with() {
        iswith = true;
    }

    public long getPrice() {
        return price;
    }
}
