import java.util.HashMap;

public class Adventurer {
    private String nameAdv;
    private int idAdv;
    private int hitPoint;
    private int level;

    private HashMap<Integer, Bottle> bottleAll;
    private HashMap<Integer, Equipment> equipmentAll;
    private HashMap<Integer, Food> foodAll;
    private Bag bag;

    public Adventurer(String name, int id) {
        this.nameAdv = name;
        this.idAdv = id;
        this.hitPoint = 500;
        this.level = 1;
        this.bottleAll = new HashMap<>();
        this.equipmentAll = new HashMap<>();
        this.foodAll = new HashMap<>();
        this.bag = new Bag(level);
    }

    public int GetLevel() {
        return this.level;
    }

    public int GetHitPoint() {
        return this.hitPoint;
    }

    //Bottle

    public void AddBottle(int botID, String botName, int botCapacity) {
        Bottle bottle = new Bottle(botID, botName, botCapacity);
        this.bottleAll.put(botID, bottle);
    }

    public void DeleteBottle(int botId) {
        System.out.print(this.bottleAll.size() - 1);
        System.out.print(" ");
        System.out.println(this.bottleAll.get(botId).GetName());

        this.bottleAll.remove(botId);
        this.bag.DeleteBottle(botId);
    }

    public int GetBottleCount() {
        return this.bottleAll.size();
    }

    public boolean CheckBottle(int botId) {
        return this.bottleAll.containsKey(botId);
    }

    public void UseBottle(String botName) {
        if (this.bag.ContainBottle(botName)) {
            int botId = this.bag.GetBottleId(botName);
            int hitChange = bag.UseBottle(botId);
            if (hitChange == 0) {
                this.bottleAll.remove(botId);
            }
            this.hitPoint += hitChange;

            System.out.print(botId);
            System.out.print(" ");
            System.out.println(this.hitPoint);
        } else {
            System.out.println("fail to use " + botName);
        }
    }

    public void TakeBottle(int botId) {
        if (this.bottleAll.containsKey(botId)) {
            this.bag.RenewBottleMax(this.level);
            this.bag.AddBottle(botId, this.bottleAll.get(botId));
        }
    }

    //Equipment

    public void AddEquipment(int equId, String equName, int equStar) {
        Equipment equipment = new Equipment(equId, equName, equStar);
        this.equipmentAll.put(equId, equipment);
    }

    public void DeleteEquipment(int equId) {
        System.out.print(this.equipmentAll.size() - 1);
        System.out.print(" ");
        System.out.println(this.equipmentAll.get(equId).GetName());

        this.equipmentAll.remove(equId);
        this.bag.DeleteEquipment(equId);
    }

    public int GetEquipmentCount() {
        return this.equipmentAll.size();
    }

    public int GetTakeEquipmentCount() {
        return this.bag.GetEquipmentCount();
    }

    public boolean CheckEquipment(int equId) {
        return this.equipmentAll.containsKey(equId);
    }

    public void AddStar(int equId) {
        Equipment equipment = equipmentAll.get(equId);
        equipment.AddStar();
        System.out.print(equipment.GetName());
        System.out.print(" ");
        System.out.println(equipment.GetStar());
    }

    public int GetStar(int equId) {
        Equipment equipment = equipmentAll.get(equId);
        return equipment.GetStar();
    }

    public void TakeEquipment(int equId) {
        if (this.equipmentAll.containsKey(equId)) {
            this.bag.AddEquipment(equId, this.equipmentAll.get(equId));
        }
    }

    //Food

    public void AddFood(int foodId, String foodName, int foodEnergy) {
        Food food = new Food(foodId, foodName, foodEnergy);
        foodAll.put(foodId, food);
    }

    public void DeleteFood(int foodId) {
        System.out.print(this.foodAll.size() - 1);
        System.out.print(" ");
        System.out.println(this.foodAll.get(foodId).GetName());

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

            System.out.print(foodId);
            System.out.print(" ");
            System.out.println(this.level);
        } else {
            System.out.println("fail to eat " + foodName);
        }
    }
}
