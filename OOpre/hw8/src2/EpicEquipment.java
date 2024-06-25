public class EpicEquipment extends Equipment implements Commodity {
    private final double ratio;

    public EpicEquipment(int paraId, String paraName, int paraStar,
                         long paraPrice, double paraRatio) {
        super(paraId, paraName, paraStar, paraPrice);
        this.ratio = paraRatio;
    }

    public double getRatio() {
        return ratio;
    }
}
