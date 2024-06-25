public class Store {
    private int soldBotCount;
    private long soldBotPrice;
    private long soldBotCapacity;
    private int soldEquCount;
    private long soldEquPrice;
    private long soldEquStar;
    private int soldFoodCount;
    private long soldFoodPrice;
    private long soldFoodEnergy;

    private static final Store store = new Store();

    private Store() {
        this.soldBotCount = 0;
        this.soldEquCount = 0;
        this.soldFoodCount = 0;
        this.soldBotPrice = 0;
        this.soldBotCapacity = 0;
        this.soldEquPrice = 0;
        this.soldEquStar = 0;
        this.soldFoodPrice = 0;
        this.soldFoodEnergy = 0;
    }

    public static Store getInstance() {
        return store;
    }

    public long sellBot(Bottle bottle) {
        soldBotCount++;
        soldBotPrice += bottle.getPrice();
        soldBotCapacity += bottle.getCapacity();
        return bottle.getPrice();
    }

    public long sellEqu(Equipment equipment) {
        soldEquCount++;
        soldEquPrice += equipment.getPrice();
        soldEquStar += equipment.getStar();
        return equipment.getPrice();
    }

    public long sellFood(Food food) {
        soldFoodCount++;
        soldFoodPrice += food.getPrice();
        soldFoodEnergy += food.getEnergy();
        return food.getPrice();
    }

    public Bottle buyBottle(long money,
                            int id, String name, String type, double ratio) {
        long botPrice = soldBotPrice / soldBotCount;
        if (botPrice > money) {
            return new Bottle(-1, " ", 0, botPrice);
        } else {
            long capacity = soldBotCapacity / soldBotCount;
            switch (type) {
                case "RegularBottle":
                    return new RegularBottle(id, name, (int) capacity, botPrice);
                case "ReinforcedBottle":
                    return new ReinforcedBottle(id, name, (int) capacity, botPrice, ratio);
                case "RecoverBottle":
                    return new RecoverBottle(id, name, (int) capacity, botPrice, ratio);
                default:
                    return null;
            }
        }
    }

    public Equipment buyEquipment(long money,
                                  int id, String name, String type, int critical, double ratio) {
        long equPrice = soldEquPrice / soldEquCount;
        if (equPrice > money) {
            return new Equipment(-1, " ", 0, equPrice);
        } else {
            long star = soldEquStar / soldEquCount;
            switch (type) {
                case "RegularEquipment":
                    return new RegularEquipment(id, name, (int) star, equPrice);
                case "CritEquipment":
                    return new CritEquipment(id, name, (int) star, equPrice, critical);
                case "EpicEquipment":
                    return new EpicEquipment(id, name, (int) star, equPrice, ratio);
                default:
                    return null;
            }
        }
    }

    public Food buyFood(long money,
                        int id, String name) {
        long foodPrice = soldFoodPrice / soldFoodCount;
        if (foodPrice > money) {
            return new Food(-1, " ", 0, foodPrice);
        } else {
            long energy = soldFoodEnergy / soldFoodCount;
            return new Food(id, name, (int) energy, foodPrice);
        }
    }
}
