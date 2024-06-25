public class Equipment {
    private int id;
    private String name;
    private int star;

    public Equipment(int id, String name, int star) {
        this.id = id;
        this.name = name;
        this.star = star;
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
}
