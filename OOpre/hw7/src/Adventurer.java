import java.util.HashMap;
import java.util.Map;

public class Adventurer implements Commodity {
    private String nameAdv;
    private int idAdv;
    private int hitPoint;
    private int hitPointBefore;
    private int level;
    private long money;
    private boolean inFight;

    private HashMap<Integer, Bottle> bottleAll;
    private HashMap<Integer, Equipment> equipmentAll;
    private HashMap<Integer, Food> foodAll;
    private HashMap<Integer, Adventurer> hireList;
    private Bag bag;

    public Adventurer(String name, int id) {
        this.nameAdv = name;
        this.idAdv = id;
        this.hitPoint = 500;
        this.hitPointBefore = 500;
        this.level = 1;
        this.money = 0;
        this.bottleAll = new HashMap<>();
        this.equipmentAll = new HashMap<>();
        this.foodAll = new HashMap<>();
        this.hireList = new HashMap<>();
        this.bag = new Bag(this.level);
        this.inFight = false;
    }

    //adventurer

    public String GetName() {
        return this.nameAdv;
    }

    public int GetId() {
        return this.idAdv;
    }

    public int GetLevel() {
        return this.level;
    }

    public int GetHitPoint() {
        return this.hitPoint;
    }

    public long GetMoney() {
        return this.money;
    }

    public void HireAdventurer(Adventurer employee) {
        this.hireList.put(employee.GetId(), employee);
    }

    //commodity

    @Override
    public long GetCommodity() {
        long commodity = 0;
        //sum bottle
        for (Map.Entry<Integer, Bottle> entry : bottleAll.entrySet()) {
            commodity += entry.getValue().GetCommodity();
        }

        //sum equipment
        for (Map.Entry<Integer, Equipment> entry : equipmentAll.entrySet()) {
            commodity += entry.getValue().GetCommodity();
        }

        //sum food
        for (Map.Entry<Integer, Food> entry : foodAll.entrySet()) {
            commodity += entry.getValue().GetCommodity();
        }

        //sum hire
        for (Map.Entry<Integer, Adventurer> entry : hireList.entrySet()) {
            commodity += entry.getValue().GetCommodity();
        }

        return commodity + this.money;
    }

    public int GetCount() {
        return this.bottleAll.size() + this.equipmentAll.size()
                + this.foodAll.size() + this.hireList.size();
    }

    public long GetMaxCommodity() {
        long maxCommodity = 0;
        //bottle
        for (Map.Entry<Integer, Bottle> entry : bottleAll.entrySet()) {
            maxCommodity = Math.max(entry.getValue().GetCommodity(), maxCommodity);
        }

        //equipment
        for (Map.Entry<Integer, Equipment> entry : equipmentAll.entrySet()) {
            maxCommodity = Math.max(entry.getValue().GetCommodity(), maxCommodity);
        }

        //food
        for (Map.Entry<Integer, Food> entry : foodAll.entrySet()) {
            maxCommodity = Math.max(entry.getValue().GetCommodity(), maxCommodity);
        }

        //hire
        for (Map.Entry<Integer, Adventurer> entry : hireList.entrySet()) {
            maxCommodity = Math.max(entry.getValue().GetCommodity(), maxCommodity);
        }

        return maxCommodity;
    }

    public void CheckInstance(int comId) {
        //bottle
        for (Map.Entry<Integer, Bottle> entry : bottleAll.entrySet()) {
            if (entry.getValue().GetId() == comId) {
                System.out.println("Commodity whose id is " + comId
                        + " belongs to " + entry.getValue().getClass().getSimpleName());
            }
        }

        //equipment
        for (Map.Entry<Integer, Equipment> entry : equipmentAll.entrySet()) {
            if (entry.getValue().GetId() == comId) {
                System.out.println("Commodity whose id is " + comId
                        + " belongs to " + entry.getValue().getClass().getSimpleName());
            }
        }

        //food
        for (Map.Entry<Integer, Food> entry : foodAll.entrySet()) {
            if (entry.getValue().GetId() == comId) {
                System.out.println("Commodity whose id is " + comId
                        + " belongs to " + entry.getValue().getClass().getSimpleName());
            }
        }

        //hire
        for (Map.Entry<Integer, Adventurer> entry : hireList.entrySet()) {
            if (entry.getValue().GetId() == comId) {
                System.out.println("Commodity whose id is " + comId
                        + " belongs to " + entry.getValue().getClass().getSimpleName());
            }
        }
    }

    //shop

    public void SellTakeAll(Shop shop) {
        long money = this.bag.SellTakeAll(shop, bottleAll, equipmentAll, foodAll);
        this.money += money;
        System.out.println(this.nameAdv + " emptied the backpack " + money);
    }

    //fight

    public boolean GetStatus() {
        return this.inFight;
    }

    public void InFight() {
        this.inFight = true;
        this.hitPointBefore = this.hitPoint;
    }

    public void GetAttacked(int fighterLevel, Equipment equipment) {
        this.hitPoint -= equipment.UseEquipment(fighterLevel, hitPoint);
    }

    public void OutFight() {
        this.inFight = false;
        if (this.hitPoint <= this.hitPointBefore / 2) {
            for (Map.Entry<Integer, Adventurer> entry : hireList.entrySet()) {
                this.money += entry.getValue().
                        GiveHelp(this.hitPointBefore - this.hitPoint);
            }
        }
        this.hitPointBefore = this.hitPoint;
    }

    public long GiveHelp(int decreaseHitPoint) {
        long help = Math.min(this.money, decreaseHitPoint * 10000L);
        this.money -= help;
        return help;
    }

    //Bottle

    public void AddBottle(Bottle bottle) {
        this.bottleAll.put(bottle.GetId(), bottle);
    }

    public void BuyBottle(Bottle bottle) {
        if (this.money >= bottle.GetCommodity()) {
            this.money -= bottle.GetCommodity();
            this.AddBottle(bottle);
            System.out.println("successfully buy " + bottle.GetName()
                    + " for " + bottle.GetCommodity());
        } else {
            System.out.println("failed to buy " + bottle.GetName()
                    + " for " + bottle.GetCommodity());
        }
    }

    public void SellBottle(int botId, Shop shop) {
        System.out.print(this.bottleAll.size() - 1);
        System.out.print(" ");
        System.out.println(this.bottleAll.get(botId).GetName());

        this.money += shop.SellBottle(this.bottleAll.get(botId));
        this.bottleAll.remove(botId);
        this.bag.DeleteBottle(botId);
    }

    public int GetBottleCount() {
        return this.bottleAll.size();
    }

    public boolean CheckBottle(int botId) {
        return this.bottleAll.containsKey(botId);
    }

    public boolean CheckTakeBottle(String botName) {
        return this.bag.ContainBottle(botName);
    }

    public void UseBottle(String botName, boolean ifInFight) {
        if (this.bag.ContainBottle(botName)) {
            int botId = this.bag.GetBottleId(botName);
            int hitChange = bag.UseBottle(botId, hitPoint);

            if (hitChange == 0) {
                this.bottleAll.remove(botId);
            }
            this.hitPoint += hitChange;

            System.out.println(botId + " " + this.hitPoint);
        } else {
            if (ifInFight) {
                System.out.println("Fight log error");
            } else {
                System.out.println("fail to use " + botName);
            }
        }
    }

    public void TakeBottle(int botId) {
        if (this.bottleAll.containsKey(botId)) {
            this.bag.RenewBottleMax(this.level);
            this.bag.AddBottle(botId, this.bottleAll.get(botId));
        }
    }

    public int GetBottleId(String botName) {
        int botId;
        if (this.bag.ContainBottle(botName)) {
            botId = this.bag.GetBottleId(botName);
        } else {
            botId = 0;
        }
        return botId;
    }

    //Equipment

    public void AddEquipment(Equipment equipment) {
        this.equipmentAll.put(equipment.GetId(), equipment);
    }

    public void BuyEquipment(Equipment equipment) {
        if (this.money >= equipment.GetCommodity()) {
            this.money -= equipment.GetCommodity();
            this.AddEquipment(equipment);
            System.out.println("successfully buy " + equipment.GetName()
                    + " for " + equipment.GetCommodity());
        } else {
            System.out.println("failed to buy " + equipment.GetName()
                    + " for " + equipment.GetCommodity());
        }
    }

    public void SellEquipment(int equId, Shop shop) {
        System.out.print((this.equipmentAll.size() - 1));
        System.out.print(" ");
        System.out.println(this.equipmentAll.get(equId).GetName());

        this.money += shop.SellEquipment(this.equipmentAll.get(equId));
        this.equipmentAll.remove(equId);
        this.bag.DeleteEquipment(equId);
    }

    public int GetEquipmentCount() {
        return this.equipmentAll.size();
    }

    public int GetTakeEquipmentCount() {
        return this.bag.GetEquipmentCount();
    }

    public Equipment GetEquipment(String equName) {
        return this.bag.GetEquipment(equName);
    }

    public boolean CheckEquipment(int equId) {
        return this.equipmentAll.containsKey(equId);
    }

    public boolean CheckTakeEquipment(String equName) {
        return this.bag.ContainEquipment(equName);
    }

    public void AddStar(int equId) {
        Equipment equipment = equipmentAll.get(equId);
        equipment.AddStar();
        System.out.println(equipment.GetName() + " " + equipment.GetStar());
    }

    public int GetStar(int equId) {
        return equipmentAll.get(equId).GetStar();
    }

    public void TakeEquipment(int equId) {
        if (this.equipmentAll.containsKey(equId)) {
            this.bag.AddEquipment(equId, this.equipmentAll.get(equId));
        }
    }

    //Food

    public void AddFood(Food food) {
        this.foodAll.put(food.GetId(), food);
    }

    public void BuyFood(Food food) {
        if (this.money >= food.GetCommodity()) {
            this.money -= food.GetCommodity();
            this.AddFood(food);
            System.out.println("successfully buy " + food.GetName()
                    + " for " + food.GetCommodity());
        } else {
            System.out.println("failed to buy " + food.GetName()
                    + " for " + food.GetCommodity());
        }
    }

    public void SellFood(int foodId, Shop shop) {
        System.out.print(this.foodAll.size() - 1);
        System.out.print(" ");
        System.out.println(this.foodAll.get(foodId).GetName());

        this.money += shop.SellFood(this.foodAll.get(foodId));
        this.foodAll.remove(foodId);
        this.bag.DeleteFood(foodId);
    }

    public int GetFoodCount() {
        return this.foodAll.size();
    }

    public void TakeFood(int foodId) {
        if (this.foodAll.containsKey(foodId)) {
            this.bag.AddFood(foodId, this.foodAll.get(foodId));
        }
    }

    public void UseFood(String foodName) {
        if (this.bag.ContainFood(foodName)) {
            int foodId = this.bag.GetFoodId(foodName);

            this.level += bag.UseFood(foodId);
            this.foodAll.remove(foodId);
            this.bag.RenewBottleMax(this.level);

            System.out.println(foodId + " " + this.level);
        } else {
            System.out.println("fail to eat " + foodName);
        }
    }
}
