public class Equipment {

    private  final int id;
    private final String name;
    private int star;
    private boolean iswith;

    private long price;

    public Equipment(int id,String name,int star,long price) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.price = price;
    }

    public boolean wheWith() {
        return iswith;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public void starUp() {
        this.star++;
    }

    public int checkStar() {
        return star;
    }

    public void with() {
        this.iswith = true;
    }

    public void nowith() {
        this.iswith = false;
    }

    public int getId() {
        return id;
    }
}
