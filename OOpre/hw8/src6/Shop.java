import java.util.ArrayList;

public class Shop {
    private static Shop instance; // 商店的单例实例
    private int foodNum;
    private int bottleNum;
    private int equipmentNum;
    private int totalEnergy;
    private int totalCapacity;
    private int totalStar;
    private Long foodMoney;
    private Long bottleMoney;
    private Long equipmentMoney;
    // 其他商店的属性和行为

    private Shop() {
        this.foodNum = 0;
        this.bottleNum = 0;
        this.equipmentNum = 0;
        this.totalEnergy = 0;
        this.totalCapacity = 0;
        this.totalStar = 0;
        this.foodMoney = 0L;
        this.bottleMoney = 0L;
        this.equipmentMoney = 0L;
    }

    public static Shop getInstance() {
        if (instance == null) {
            instance = new Shop();
        }
        return instance;
    }

    public void sellItem(String type,int attribute,Long money) {
        if (type.equals("food")) {
            this.foodNum++;
            this.foodMoney += money;
            this.totalEnergy += attribute;
        }
        else if (type.equals("bottle")) {
            this.bottleNum++;
            this.bottleMoney += money;
            this.totalCapacity += attribute;
        }
        else {
            this.equipmentNum++;
            this.equipmentMoney += money;
            this.totalStar += attribute;
        }
    }

    public void buyItem(ArrayList<String> strings) {

    }

    public long getItemPrice(String string) {
        if (string.equals("Food")) {
            return (long)Math.floor((double) foodMoney / foodNum);
        }
        else if (string.equals("RegularBottle") || string.equals("RecoverBottle") ||
                string.equals("ReinforcedBottle")) {
            return (long)Math.floor((double) bottleMoney / bottleNum);
        }
        else {
            return (long)Math.floor((double) equipmentMoney / equipmentNum);
        }
    }

    public int getAttribute(String string) {
        if (string.equals("Food")) {
            return (int)Math.floor((double) totalEnergy / foodNum);
        }
        else if (string.equals("RegularBottle") || string.equals("RecoverBottle") ||
                string.equals("ReinforcedBottle")) {
            return (int)Math.floor((double) totalCapacity / bottleNum);
        }
        else {
            return (int)Math.floor((double) totalStar / equipmentNum);
        }
    }
}
