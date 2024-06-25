import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Adventurer implements Commodity {
    private final int id;
    private final String name;
    private int hitPoint;
    private int level;
    private long money;
    private final HashMap<Integer, Bottle> bottles;
    private final HashMap<Integer, Equipment> equipments;
    private final HashMap<Integer, Food> foods;
    private final HashMap<String, Equipment> bagEquipments;
    private final HashMap<String, HashSet<Bottle>> bagBottles;
    private final HashMap<String, HashSet<Food>> bagFoods;
    private final HashMap<Integer, Adventurer> hireAdv;

    public Adventurer(int paraId, String paraName) {
        this.id = paraId;
        this.name = paraName;
        this.hitPoint = 500;
        this.level = 1;
        this.money = 0;
        this.bottles = new HashMap<>();
        this.equipments = new HashMap<>();
        this.foods = new HashMap<>();
        this.bagEquipments = new HashMap<>();
        this.bagBottles = new HashMap<>();
        this.bagFoods = new HashMap<>();
        this.hireAdv = new HashMap<>();
    }

    @Override
    public long getPrice() {
        long price = 0;
        for (Adventurer adventurer : hireAdv.values()) {
            price += adventurer.getPrice() + adventurer.getMoney();
        }
        for (Bottle bottle : bottles.values()) {
            price += bottle.getPrice();
        }
        for (Equipment equipment : equipments.values()) {
            price += equipment.getPrice();
        }
        for (Food food : foods.values()) {
            price += food.getPrice();
        }
        return price;
    }

    public long getMoney() {
        return money;
    }

    public int getPriceCount() {
        return bottles.size() + equipments.size() + foods.size() +
                hireAdv.size();
    }

    public long getPriceMax() {
        long priceMax = 0;
        for (Adventurer adventurer : hireAdv.values()) {
            priceMax = Math.max(adventurer.getPrice() + adventurer.getMoney(), priceMax);
        }
        for (Bottle bottle : bottles.values()) {
            priceMax = Math.max(bottle.getPrice(), priceMax);
        }
        for (Equipment equipment : equipments.values()) {
            priceMax = Math.max(equipment.getPrice(), priceMax);
        }
        for (Food food : foods.values()) {
            priceMax = Math.max(food.getPrice(), priceMax);
        }
        return priceMax;
    }

    public Commodity getComPrice(int comId) {
        if (hireAdv.containsKey(comId)) {
            return hireAdv.get(comId);
        } else if (bottles.containsKey(comId)) {
            return bottles.get(comId);
        } else if (equipments.containsKey(comId)) {
            return equipments.get(comId);
        } else {
            return foods.getOrDefault(comId, null);
        }
    }

    public boolean hireAdv(Adventurer hiredAdv) {
        hireAdv.put(hiredAdv.getId(), hiredAdv);
        return true;
    }

    public boolean addBot(int botId, String name, int capacity,
                          long price, String type, double ratio) {
        Bottle newBot;
        switch (type) {
            case "RegularBottle":
                newBot = new RegularBottle(botId, name, capacity, price);
                break;
            case "ReinforcedBottle":
                newBot = new ReinforcedBottle(botId, name, capacity, price, ratio);
                break;
            case "RecoverBottle":
                newBot = new RecoverBottle(botId, name, capacity, price, ratio);
                break;
            default:
                return false;
        }
        bottles.put(botId, newBot);
        return true;
    }

    public String getBotName(int botId) {
        return bottles.get(botId).getName();
    }

    public boolean subBot(int botId) {
        money += Store.getInstance().sellBot(bottles.get(botId));
        if (bagBottles.containsKey(bottles.get(botId).getName())) {
            bagBottles.get(bottles.get(botId).getName()).remove(bottles.get(botId));
            if (bagBottles.get(bottles.get(botId).getName()).isEmpty()) {
                bagBottles.remove(bottles.get(botId).getName());
            }
        }
        bottles.remove(botId);
        return true;
    }

    public int getBotNum() {
        return bottles.size();
    }

    public boolean addEqu(int equId, String name, int star,
                          long price, String type, int critical, double ratio) {
        Equipment newEqu;
        switch (type) {
            case "RegularEquipment":
                newEqu = new RegularEquipment(equId, name, star, price);
                break;
            case "CritEquipment":
                newEqu = new CritEquipment(equId, name, star, price, critical);
                break;
            case "EpicEquipment":
                newEqu = new EpicEquipment(equId, name, star, price, ratio);
                break;
            default:
                return false;
        }
        equipments.put(equId, newEqu);
        return true;
    }

    public String getEquName(int equId) {
        return equipments.get(equId).getName();
    }

    public boolean subEqu(int equId) {
        money += Store.getInstance().sellEqu(equipments.get(equId));
        if (bagEquipments.containsValue(equipments.get(equId))) {
            bagEquipments.remove(equipments.get(equId).getName());
        }
        equipments.remove(equId);
        return true;
    }

    public int getEquNum() {
        return equipments.size();
    }

    public boolean addEquStar(int equId) {
        equipments.get(equId).addStar();
        return true;
    }

    public int getEquStar(int equId) {
        return equipments.get(equId).getStar();
    }

    public boolean addFood(int foodId, String name, int energy,
                           long price) {
        Food newFood = new Food(foodId, name, energy, price);
        foods.put(foodId, newFood);
        return true;
    }

    public String getFoodName(int foodId) {
        return foods.get(foodId).getName();
    }

    public boolean subFood(int foodId) {
        money += Store.getInstance().sellFood(foods.get(foodId));
        if (bagFoods.containsKey(foods.get(foodId).getName())) {
            bagFoods.get(foods.get(foodId).getName()).remove(foods.get(foodId));
            if (bagFoods.get(foods.get(foodId).getName()).isEmpty()) {
                bagFoods.remove(foods.get(foodId).getName());
            }
        }
        foods.remove(foodId);
        return true;
    }

    public int getFoodNum() {
        return foods.size();
    }

    public boolean carryEqu(int equId) {
        Equipment wantEqu = equipments.get(equId);
        if (bagEquipments.containsKey(wantEqu.getName())) {
            bagEquipments.replace(wantEqu.getName(), wantEqu);
        } else {
            bagEquipments.put(wantEqu.getName(), wantEqu);
        }
        return true;
    }

    public boolean carryBot(int botId) {
        int maxBottles = (level / 5 + 1);
        Bottle wantBot = bottles.get(botId);
        if (bagBottles.containsKey(wantBot.getName())) {
            HashSet<Bottle> nowBottleSet = bagBottles.get(wantBot.getName());
            if (nowBottleSet.size() < maxBottles) {
                nowBottleSet.add(wantBot);
                return true;
            } else {
                return false;
            }
        } else {
            HashSet<Bottle> newBottleSet = new HashSet<>();
            newBottleSet.add(wantBot);
            bagBottles.put(wantBot.getName(), newBottleSet);
            return true;
        }
    }

    public boolean carryFood(int foodId) {
        Food wantFood = foods.get(foodId);
        if (bagFoods.containsKey(wantFood.getName())) {
            HashSet<Food> nowFoodSet = bagFoods.get(wantFood.getName());
            nowFoodSet.add(wantFood);
        } else {
            HashSet<Food> newFoodSet = new HashSet<>();
            newFoodSet.add(wantFood);
            bagFoods.put(wantFood.getName(), newFoodSet);
        }
        return true;
    }

    public int useBot(String name) {
        if (bagBottles.containsKey(name)) {
            Bottle minIdBot = null;
            for (Bottle item : bagBottles.get(name)) {
                if (minIdBot == null || item.getId() < minIdBot.getId()) {
                    minIdBot = item;
                }
            }
            if (minIdBot.getIsEmpty()) {
                bagBottles.get(name).remove(minIdBot);
                if (bagBottles.get(name).isEmpty()) {
                    bagBottles.remove(name);
                }
                bottles.remove(minIdBot.getId());
            } else {
                if (minIdBot instanceof RegularBottle) {
                    RegularBottle regBot = (RegularBottle) minIdBot;
                    hitPoint += regBot.getCapacity();
                } else if (minIdBot instanceof ReinforcedBottle) {
                    ReinforcedBottle reiBot = (ReinforcedBottle) minIdBot;
                    hitPoint += (int) ((1 + reiBot.getRatio()) * reiBot.getCapacity());
                } else if (minIdBot instanceof RecoverBottle) {
                    RecoverBottle recBot = (RecoverBottle) minIdBot;
                    hitPoint += (int) (recBot.getRatio() * hitPoint);
                }
                minIdBot.setIsEmpty(true);
            }
            return minIdBot.getId();
        } else {
            return -1;
        }
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public int useFood(String name) {
        if (bagFoods.containsKey(name)) {
            Food minIdFood = null;
            for (Food item : bagFoods.get(name)) {
                if (minIdFood == null || item.getId() < minIdFood.getId()) {
                    minIdFood = item;
                }
            }
            level = level + minIdFood.getEnergy();
            bagFoods.get(name).remove(minIdFood);
            if (bagFoods.get(name).isEmpty()) {
                bagFoods.remove(name);
            }
            foods.remove(minIdFood.getId());
            return minIdFood.getId();
        } else {
            return -1;
        }
    }

    public int getLevel() {
        return level;
    }

    public Equipment useEqu(String equName) {
        return bagEquipments.getOrDefault(equName, null);
    }

    public void beAttacked(Equipment attackerEqu, int attackerLevel) {
        if (attackerEqu instanceof RegularEquipment) {
            RegularEquipment regEqu = (RegularEquipment) attackerEqu;
            hitPoint -= regEqu.getStar() * attackerLevel;
        } else if (attackerEqu instanceof CritEquipment) {
            CritEquipment criEqu = (CritEquipment) attackerEqu;
            hitPoint -= criEqu.getStar() * attackerLevel + criEqu.getCritical();
        } else if (attackerEqu instanceof EpicEquipment) {
            EpicEquipment epiEqu = (EpicEquipment) attackerEqu;
            hitPoint -= (int) (hitPoint * epiEqu.getRatio());
        }
    }

    public int getId() {
        return id;
    }

    public long sellAll() {
        final long originMoney = money;
        Iterator<Map.Entry<String, Equipment>> equIte = bagEquipments.entrySet().iterator();
        while (equIte.hasNext()) {
            Equipment equipment = equIte.next().getValue();
            money += Store.getInstance().sellEqu(equipment);
            equipments.remove(equipment.getId());
            equIte.remove();
        }
        Iterator<Map.Entry<String, HashSet<Bottle>>> botMapIte = bagBottles.entrySet().iterator();
        while (botMapIte.hasNext()) {
            HashSet<Bottle> botMap = botMapIte.next().getValue();
            Iterator<Bottle> botIte = botMap.iterator();
            while (botIte.hasNext()) {
                Bottle bottle = botIte.next();
                money += Store.getInstance().sellBot(bottle);
                bottles.remove(bottle.getId());
                botIte.remove();
            }
            botMapIte.remove();
        }
        Iterator<Map.Entry<String, HashSet<Food>>> foodMapIte = bagFoods.entrySet().iterator();
        while (foodMapIte.hasNext()) {
            HashSet<Food> foodMap = foodMapIte.next().getValue();
            Iterator<Food> foodIte = foodMap.iterator();
            while (foodIte.hasNext()) {
                Food food = foodIte.next();
                money += Store.getInstance().sellFood(food);
                foods.remove(food.getId());
                foodIte.remove();
            }
            foodMapIte.remove();
        }
        return money - originMoney;
    }

    public long buyFromStore(int id, String name, String type, int critical, double ratio) {
        if (type.contains("Bottle")) {
            Bottle boughtBot = Store.getInstance().buyBottle(money,
                    id, name, type, ratio);
            if (boughtBot.getId() != -1) {
                bottles.put(id, boughtBot);
                money -= boughtBot.getPrice();
                return boughtBot.getPrice();
            } else {
                return -boughtBot.getPrice();
            }
        } else if (type.contains("Equipment")) {
            Equipment boughtEqu = Store.getInstance().buyEquipment(money,
                    id, name, type, critical, ratio);
            if (boughtEqu.getId() != -1) {
                equipments.put(id, boughtEqu);
                money -= boughtEqu.getPrice();
                return boughtEqu.getPrice();
            } else {
                return -boughtEqu.getPrice();
            }
        } else if (type.equals("Food")) {
            Food boughtfood = Store.getInstance().buyFood(money,
                    id, name);
            if (boughtfood.getId() != -1) {
                foods.put(id, boughtfood);
                money -= boughtfood.getPrice();
                return boughtfood.getPrice();
            } else {
                return -boughtfood.getPrice();
            }
        } else {
            return -1;
        }
    }

    public String getName() {
        return name;
    }

    public void requireMoney(int originHitPoint) {
        int loseHitPoint = originHitPoint - hitPoint;
        for (Adventurer hireAdv : hireAdv.values()) {
            money += hireAdv.giveMoney(loseHitPoint);
        }
    }

    public long giveMoney(int loseHitPoint) {
        long requiredMoney = (long) loseHitPoint * 10000L;
        if (requiredMoney > money) {
            requiredMoney = money;
            money = 0;
        } else {
            money -= requiredMoney;
        }
        return requiredMoney;
    }

    // 测试专用Getter-Setter，应该检查确保其只用在了Test类中
    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public HashMap<Integer, Bottle> getBottles() {
        return bottles;
    }

    public HashMap<Integer, Equipment> getEquipments() {
        return equipments;
    }

    public HashMap<Integer, Food> getFoods() {
        return foods;
    }

    public HashMap<String, Equipment> getBagEquipments() {
        return bagEquipments;
    }

    public HashMap<String, HashSet<Bottle>> getBagBottles() {
        return bagBottles;
    }

    public HashMap<String, HashSet<Food>> getBagFoods() {
        return bagFoods;
    }
}
