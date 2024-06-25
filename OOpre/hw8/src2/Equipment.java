public class Equipment implements Commodity {
    // 整数、取值范围：0-2147483647
    private final int id;
    // 字符串、保证不会出现空白字符，长度区间: (0,40)
    private final String name;
    // 整数、取值范围：0 - 2147483647
    private int star;
    private final long price;

    public Equipment(int paraId, String paraName, int paraStar, long paraPrice) {
        this.id = paraId;
        this.name = paraName;
        this.star = paraStar;
        this.price = paraPrice;
    }

    @Override
    public long getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addStar() {
        this.star++;
    }

    public int getStar() {
        return star;
    }
}
