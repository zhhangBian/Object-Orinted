import java.math.BigInteger;

public class Shop {
    private BigInteger numOfFood;
    private BigInteger sumPriceOfFood;
    private BigInteger sumEnergy;
    private BigInteger nukOfBottle;
    private BigInteger sumPriceBottle;
    private BigInteger sumCapacity;
    private BigInteger numOfEquipment;
    private BigInteger sumPriceOfEquipment;
    private  BigInteger sumStar;

    private BigInteger one;

    private Shop() {
        numOfFood = new BigInteger("0");
        sumPriceOfFood = new BigInteger("0");
        sumEnergy = new BigInteger("0");

        nukOfBottle = new BigInteger("0");
        sumPriceBottle = new BigInteger("0");
        sumCapacity = new BigInteger("0");

        numOfEquipment = new BigInteger("0");
        sumPriceOfEquipment = new BigInteger("0");
        sumStar = new BigInteger("0");

        one = new BigInteger("1");
    }

    private static Shop shop = new Shop();

    public static Shop getShop() {
        return shop;
    }

    public void acceptFromAdv(Object obj) {
        if (obj instanceof Food) {
            Food food = (Food) obj;
            BigInteger d = one;
            shop.numOfFood = shop.numOfFood.add(d);
            d = BigInteger.valueOf(food.getEnergy());
            shop.sumEnergy = shop.sumEnergy.add(d);
            d = BigInteger.valueOf(food.getPrice());
            shop.sumPriceOfFood = shop.sumPriceOfFood.add(d);
        }
        else if (obj instanceof Equipment) {
            Equipment equipment = (Equipment) obj;
            BigInteger d = one;
            shop.numOfEquipment = shop.numOfEquipment.add(d);
            d = BigInteger.valueOf(equipment.getPrice());
            shop.sumPriceOfEquipment = shop.sumPriceOfEquipment.add(d);
            d = BigInteger.valueOf(equipment.checkStar());
            shop.sumStar = shop.sumStar.add(d);
        }
        else if (obj instanceof Bottle) {
            Bottle bottle = (Bottle) obj;
            BigInteger d = one;
            shop.nukOfBottle = shop.nukOfBottle.add(d);
            d = BigInteger.valueOf(bottle.getPrice());
            shop.sumPriceBottle = shop.sumPriceBottle.add(d);
            d = BigInteger.valueOf(bottle.getCapacity());
            shop.sumCapacity = shop.sumCapacity.add(d);
        }
    }

    public long getSoldPrice(String type) {
        if (type.equals("Food")) {
            return shop.sumPriceOfFood.divide(shop.numOfFood).longValue();
        }
        else if (type.equals("Equipment")) {
            return  shop.sumPriceOfEquipment.divide(shop.numOfEquipment).longValue();
        }
        else {
            return  shop.sumPriceBottle.divide(shop.nukOfBottle).longValue();
        }
    }

    public int getSoldStar() {
        return shop.sumStar.divide(shop.numOfEquipment).intValue();
    }

    public int getSoldCapacity() {
        return shop.sumCapacity.divide(shop.nukOfBottle).intValue();
    }

    public int getSoldEnergy() {
        return shop.sumEnergy.divide(shop.numOfFood).intValue();
    }
}
