public class RecoverBottle extends Bottle {
    private final double ratio;

    public RecoverBottle(int paraId, String paraName, int paraCapacity,
                         long paraPrice, double paraRatio) {
        super(paraId, paraName, paraCapacity, paraPrice);
        this.ratio = paraRatio;
    }

    public double getRatio() {
        return ratio;
    }
}
