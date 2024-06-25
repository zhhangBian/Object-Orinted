import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bag {
    private int bottleMaxCount;
    private HashMap<Integer, Bottle> bottleBag = new HashMap<>();
    private HashMap<Integer, Equipment> equipmentBag = new HashMap<>();
    private HashMap<Integer, Food> foodBag = new HashMap<>();

    public Bag(int advLevel) {
        this.bottleMaxCount = advLevel / 5 + 1;
    }

    public void RenewBottleMax(int advLevel) {
        this.bottleMaxCount = advLevel / 5 + 1;
    }

    //shop

    public long SellTakeAll(Shop shop,
                            HashMap<Integer, Bottle> bottleAll,
                            HashMap<Integer, Equipment> equipmentAll,
                            HashMap<Integer, Food> foodAll) {
        long money = 0;

        //bottle
        for (Map.Entry<Integer, Bottle> entry : bottleBag.entrySet()) {
            money += shop.SellBottle(entry.getValue());
            bottleAll.remove(entry.getValue().GetId());
        }
        this.bottleBag.clear();

        //equipment
        for (Map.Entry<Integer, Equipment> entry : equipmentBag.entrySet()) {
            money += shop.SellEquipment(entry.getValue());
            equipmentAll.remove(entry.getValue().GetId());
        }
        this.equipmentBag.clear();

        //food
        for (Map.Entry<Integer, Food> entry : foodBag.entrySet()) {
            money += shop.SellFood(entry.getValue());
            foodAll.remove(entry.getValue().GetId());
        }
        this.foodBag.clear();

        return money;
    }

    //Equipment

    public void AddEquipment(int equId, Equipment equipment) {
        if (this.ContainEquipment(equipment.GetName())) {
            int id = 0;
            for (Map.Entry<Integer, Equipment> entry : equipmentBag.entrySet()) {
                if (Objects.equals(equipment.GetName(), entry.getValue().GetName())) {
                    id = entry.getValue().GetId();
                }
            }
            this.equipmentBag.remove(id);
        }
        this.equipmentBag.put(equId, equipment);
    }

    public void DeleteEquipment(int equID) {
        this.equipmentBag.remove(equID);
    }

    public boolean ContainEquipment(String equName) {
        int flag = 0;
        for (Map.Entry<Integer, Equipment> entry : equipmentBag.entrySet()) {
            if (Objects.equals(equName, entry.getValue().GetName())) {
                flag = 1;
            }
        }
        return flag == 1;
    }

    public int GetEquipmentID(String equName) {
        if (this.ContainEquipment(equName)) {
            int idMin = 2147483647;
            for (Map.Entry<Integer, Equipment> entry : equipmentBag.entrySet()) {
                if (equName.equals(entry.getValue().GetName())
                        && entry.getValue().GetId() < idMin) {
                    idMin = entry.getValue().GetId();
                }
            }
            return idMin;
        } else {
            return 0;
        }
    }

    public Equipment GetEquipment(String equName) {
        return equipmentBag.get(GetEquipmentID(equName));
    }

    public int GetEquipmentCount() {
        return this.equipmentBag.size();
    }

    //Bottle

    public void AddBottle(int bottleId, Bottle bottle) {
        String name = bottle.GetName();
        int count = 0;
        for (Map.Entry<Integer, Bottle> entry : bottleBag.entrySet()) {
            if (name.equals(entry.getValue().GetName())) {
                count++;
            }
        }

        if (count < this.bottleMaxCount) {
            bottleBag.put(bottleId, bottle);
        }
    }

    public void DeleteBottle(int botId) {
        this.bottleBag.remove(botId);
    }

    public boolean ContainBottle(String botName) {
        int flag = 0;
        for (Map.Entry<Integer, Bottle> entry : bottleBag.entrySet()) {
            if (botName.equals(entry.getValue().GetName())) {
                flag = 1;
            }
        }
        return flag == 1;
    }

    public int GetBottleId(String botName) {
        if (this.ContainBottle(botName)) {
            int idMin = 2147483647;
            for (Map.Entry<Integer, Bottle> entry : bottleBag.entrySet()) {
                if (botName.equals(entry.getValue().GetName())
                        && entry.getValue().GetId() < idMin) {
                    idMin = entry.getValue().GetId();
                }
            }
            return idMin;
        } else {
            return 0;
        }
    }

    public int UseBottle(int botId, int hitPoint) {
        if (bottleBag.containsKey(botId)) {
            Bottle bottle = bottleBag.get(botId);
            if (bottle.GetIsEmpty()) {
                bottleBag.remove(botId);
                return 0;
            } else {
                return bottle.UseBottle(hitPoint);
            }
        } else {
            return 0;
        }
    }

    public boolean GetBottleIsEmpty(int botID) {
        return this.bottleBag.get(botID).GetIsEmpty();
    }

    //Food

    public void AddFood(int foodId, Food food) {
        this.foodBag.put(foodId, food);
    }

    public void DeleteFood(int foodId) {
        this.foodBag.remove(foodId);
    }

    public int UseFood(int foodId) {
        if (foodBag.containsKey(foodId)) {
            int energy = foodBag.get(foodId).GetEnergy();
            foodBag.remove(foodId);
            return energy;
        } else {
            return 0;
        }
    }

    public boolean ContainFood(String foodName) {
        int flag = 0;
        for (Map.Entry<Integer, Food> entry : foodBag.entrySet()) {
            if (foodName.equals(entry.getValue().GetName())) {
                flag = 1;
            }
        }

        return flag == 1;
    }

    public int GetFoodId(String foodName) {
        if (this.ContainFood(foodName)) {
            int idMin = 2147483647;
            for (Map.Entry<Integer, Food> entry : foodBag.entrySet()) {
                if (foodName.equals(entry.getValue().GetName())
                        && entry.getValue().GetId() < idMin) {
                    idMin = entry.getValue().GetId();
                }
            }
            return idMin;
        } else {
            return 0;
        }
    }
}
