public class Bottle {
    private int id;
    private String name;
    private int capacity;

    public Bottle(int idNew, String nameNew, int capacityNew) {
        this.id = idNew;
        this.name = nameNew;
        this.capacity = capacityNew;
    }

    public String GetName() {
        return this.name;
    }
}
