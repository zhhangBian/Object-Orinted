public class Equipment implements ICommodity {
    private final int id;
    private final String name;
    private int star;
    private final long price;

    public Equipment(int id, String name, int star, long price) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getStar() {
        return star;
    }

    public void enhanceStar() {
        star += 1;
    }

    public int getDamage(int level) {
        return star * level;
    }

    @Override
    public String getBelonging() {
        return null;
    }

    @Override
    public long getCommodity() {
        return price;
    }

    @Override
    public int getAttribute() {
        return star;
    }
}
