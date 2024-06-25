public class CritEquipment extends Equipment {
    private final int critical;

    public CritEquipment(int paraId, String paraName, int paraStar,
                         long paraPrice, int paraCritical) {
        super(paraId, paraName, paraStar, paraPrice);
        this.critical = paraCritical;
    }

    public int getCritical() {
        return critical;
    }
}
