import java.util.ArrayList;
import java.util.HashMap;

public class Adventurer {
    private final int id;
    private final String name;
    private int level;
    private int hitpoint;
    private long money;
    private int numOfBottle;
    private int idTemBottle;

    private int savedHitPoint;
    private HashMap<Integer,Bottle> bottleHashMap;
    private HashMap<String, ArrayList<Bottle>> bagForBottle;
    private int numOfEquipment;
    private HashMap<Integer,Equipment> equippmentHashMap;
    private HashMap<String,Equipment> bagForEquipment;
    private int numOfFood;
    private HashMap<Integer,Food> foodHashMap;
    private HashMap<String,ArrayList<Food>> bagForFood;
    private ArrayList<Integer> workers;
    private Demo demo;
    private Shop shop;

    public Adventurer(int id,String name,Demo demo) {
        this.id = id;
        this.name = name;
        this.hitpoint = 500;
        this.level = 1;
        this.money = 0;
        this.demo = demo;
        this.shop = Shop.getShop();
        this.numOfBottle = 0;
        this.idTemBottle = 0;
        bottleHashMap = new HashMap<>();
        bagForBottle = new HashMap<>();
        this.numOfEquipment = 0;
        equippmentHashMap = new HashMap<>();
        bagForEquipment = new HashMap<>();
        this.numOfFood = 0;
        foodHashMap = new HashMap<>();
        bagForFood = new HashMap<>();
        this.workers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHitpoint() {
        return hitpoint;
    }

    public void addBottle(int id,String name,int capacity,long price,String type,double ratio) {
        if (type.equals("RegularBottle")) {
            Bottle newBottle = new Bottle(id, name, capacity, price);
            bottleHashMap.put(id, newBottle);
        }
        else if (type.equals("ReinforcedBottle")) {
            Bottle newBottle = new ReinforcedBottle(id, name, capacity, price,ratio);
            bottleHashMap.put(id, newBottle);
        }
        else if (type.equals("RecoverBottle")) {
            Bottle newBottle = new RecoverBottle(id, name, capacity, price,ratio);
            bottleHashMap.put(id, newBottle);
        }
        numOfBottle++;
    }

    public void deleteBottle(int id,boolean wheOut,boolean wheSold) {
        Bottle temBottle = bottleHashMap.get(id);
        if (wheSold) {
            shop.acceptFromAdv(temBottle);
            this.money += temBottle.getPrice();
        }
        String name = temBottle.getName();
        if (temBottle.whetherWith()) {
            this.deleteBottleInBag(id,name);
        }
        bottleHashMap.remove(id,temBottle);
        numOfBottle--;
        if (wheOut) { System.out.println(numOfBottle + " " + name); }
    }

    public void deleteBottleInBag(int id,String name) {
        ArrayList<Bottle> temBottleArray = bagForBottle.get(name);
        Bottle temBottle = null;
        for (Bottle i : temBottleArray) {
            if (i.getId() == id) {
                temBottle = i;
                break;
            }
        }
        temBottleArray.remove(temBottle);
        if (temBottleArray.isEmpty()) { bagForBottle.remove(name,temBottleArray); }
    }

    public void carryBottle(int idB) {
        Bottle temBottle = bottleHashMap.get(idB);
        if (!temBottle.whetherWith()) {
            String name = temBottle.getName();
            if (bagForBottle.containsKey(name)) {
                ArrayList<Bottle> temBottles = bagForBottle.get(name);
                if (temBottles.size() < (level / 5 + 1)) {
                    temBottles.add(temBottle);
                    temBottle.with();
                }
            }
            else {
                ArrayList<Bottle> temBottles = new ArrayList<>();
                temBottles.add(temBottle);
                bagForBottle.put(name,temBottles);
                temBottle.with();
            }
        }
    }

    public void useBottle(String name,boolean wheOut) {
        int id = -1;
        ArrayList<Bottle> temBottles = bagForBottle.get(name);
        if (temBottles == null) { System.out.println("fail to use " + name); }
        else {
            for (Bottle i : temBottles) {
                if (id < 0) { id = i.getId(); }
                else { id = Math.min(id, i.getId()); }
            }
            Bottle bottleToUse = bottleHashMap.get(id);
            if (bottleToUse.whetherEmpty()) { deleteBottle(id,false,false); }
            else {
                int change;
                if (bottleToUse instanceof ReinforcedBottle) {
                    ReinforcedBottle temBottle = (ReinforcedBottle) bottleToUse;
                    change = (int)(temBottle.getRatio() * temBottle.getCapacity());
                    change += temBottle.getCapacity();
                }
                else if (bottleToUse instanceof RecoverBottle) {
                    RecoverBottle temBottle = (RecoverBottle) bottleToUse;
                    change = (int)(temBottle.getRatio() * hitpoint);
                }
                else { change = bottleToUse.getCapacity(); }
                this.hitpoint += change;
                bottleToUse.drunk();
            }
            if (wheOut) { System.out.println(id + " " + hitpoint); }
            else { idTemBottle = id; }
        }
    }

    public int getIdTemBottle() {
        return idTemBottle;
    }

    public boolean whetherWithBottle(String name) {
        return bagForBottle.containsKey(name);
    }

    public void addEquipment(int id,String name,int star,long price,int cri,double ratio,String t) {
        if (t.equals("RegularEquipment")) {
            Equipment newEquipment = new Equipment(id, name, star, price);
            equippmentHashMap.put(id,newEquipment);
        }
        else if (t.equals("CritEquipment")) {
            Equipment newEquipment = new CritEquipment(id, name, star, price,cri);
            equippmentHashMap.put(id,newEquipment);
        }
        else if (t.equals("EpicEquipment")) {
            Equipment newEquipment = new EpicEquipment(id, name, star, price,ratio);
            equippmentHashMap.put(id,newEquipment);
        }
        numOfEquipment++;
    }

    public void deleteEquipmentInBag(String name) {
        Equipment temEquipment = bagForEquipment.get(name);
        temEquipment.nowith();
        bagForEquipment.remove(name,temEquipment);
        bagForEquipment.remove(name);
    }

    public void soldEquipment(int id,boolean wheOut) {
        Equipment temEquipment = equippmentHashMap.get(id);
        shop.acceptFromAdv(temEquipment);
        this.money += temEquipment.getPrice();
        String name = temEquipment.getName();
        if (temEquipment.wheWith()) { deleteEquipmentInBag(name); }
        equippmentHashMap.remove(id,temEquipment);
        numOfEquipment--;
        if (wheOut) { System.out.println(numOfEquipment + " " + name); }
    }

    public void addStarOfEquipment(int id) {
        Equipment tenEquipment = equippmentHashMap.get(id);
        String name = tenEquipment.getName();
        tenEquipment.starUp();;
        int newStar = tenEquipment.checkStar();
        System.out.println(name + " " + newStar);
    }

    public void carryEquipment(int id) {
        Equipment temEquipment = equippmentHashMap.get(id);
        if (!temEquipment.wheWith()) {
            temEquipment.with();
            String name = temEquipment.getName();
            if (bagForEquipment.containsKey(name)) {
                Equipment equipmentOther = bagForEquipment.get(name);
                equipmentOther.nowith();
                bagForEquipment.replace(name, temEquipment);
            } else { bagForEquipment.put(name, temEquipment); }
        }
    }

    public Equipment getEquipment(String name) {
        return bagForEquipment.get(name);
    }

    public void addFood(int id,String name,int energy,long price) {
        Food newFood = new Food(id,name,energy,price);
        foodHashMap.put(id,newFood);
        numOfFood++;
    }

    public void deleteFood(int id,boolean wheOut) {
        Food temFood = foodHashMap.get(id);
        String name = temFood.getName();
        if (temFood.whetherWith()) { this.deleteFoodInBag(id,name); }
        foodHashMap.remove(id,temFood);
        numOfFood--;
        if (wheOut) { System.out.println(numOfFood + " " + name); }
    }

    public void deleteFoodInBag(int id,String name) {
        ArrayList<Food> temFoodArray = bagForFood.get(name);
        Food temFood = null;
        for (Food i : temFoodArray) {
            if (i.getId() == id) {
                temFood = i;
                break;
            }
        }
        temFoodArray.remove(temFood);
        if (temFoodArray.isEmpty()) { bagForFood.remove(name,temFoodArray); }
    }

    public void  soldFood(int id,boolean wheOut) {
        Food temFood = foodHashMap.get(id);
        shop.acceptFromAdv(temFood);
        this.money += temFood.getPrice();
        String name = temFood.getName();
        if (temFood.whetherWith()) { this.deleteFoodInBag(id,name); }
        foodHashMap.remove(id,temFood);
        numOfFood--;
        if (wheOut) { System.out.println(numOfFood + " " + name); }
    }

    public void carryFood(int id) {
        Food temFood = foodHashMap.get(id);
        if (!temFood.whetherWith()) {
            String name = temFood.getName();
            if (bagForFood.containsKey(name)) {
                bagForFood.get(name).add(temFood);
                temFood.with();
            }
            else {
                ArrayList<Food> temFoods = new ArrayList<>();
                temFoods.add(temFood);
                bagForFood.put(name,temFoods);
                temFood.with();
            }
        }
    }

    public void useFood(String name) {
        int id = -1;
        ArrayList<Food> temFoods = bagForFood.get(name);
        if (temFoods == null) { System.out.println("fail to eat " + name); }
        else {
            for (Food i : temFoods) {
                if (id < 0) { id = i.getId(); }
                else { id = Math.min(id, i.getId()); }
            }
            Food foodToUse = foodHashMap.get(id);
            deleteFood(id,false);
            int change = foodToUse.getEnergy();
            this.level += change;
            System.out.println(id + " " + level);
        }
    }

    public void getHurt(int level,Equipment temEquipment) {
        int hit;
        if (temEquipment instanceof EpicEquipment) {
            EpicEquipment temEpicEquipment = (EpicEquipment) temEquipment;
            double ratio = temEpicEquipment.getRatio();
            hit = (int)(ratio * this.hitpoint);
        }
        else if (temEquipment instanceof CritEquipment) {
            CritEquipment temCritEquipment = (CritEquipment) temEquipment;
            int critical = temCritEquipment.getCritical();
            hit = temCritEquipment.checkStar() * level + critical;
        }
        else { hit = temEquipment.checkStar() * level; }
        hitpoint -= hit;
    }

    public void payForAdventurer(int id) {
        if (!workers.contains(id)) { workers.add(id); }
    }

    public long getPriceSum(boolean wheOut) {
        long priceSum = 0;
        long numOfCom = 0;
        numOfCom += bottleHashMap.size();
        numOfCom += equippmentHashMap.size();
        numOfCom += workers.size();
        numOfCom += foodHashMap.size();
        for (Bottle i : bottleHashMap.values()) { priceSum += i.getPrice(); }
        for (Equipment i : equippmentHashMap.values()) { priceSum += i.getPrice(); }
        for (Food i : foodHashMap.values()) { priceSum += i.getPrice(); }
        for (int i : workers) { priceSum += demo.getPriceOfAdventurer(i); }
        if (wheOut) {
            System.out.println(numOfCom + " " + priceSum);
            return priceSum;
        }
        else { return priceSum + money; }
    }

    public void checkMostCom() {
        long mostCom = 0;
        for (Bottle i : bottleHashMap.values()) { mostCom = Math.max(mostCom,i.getPrice()); }
        for (Equipment i : equippmentHashMap.values()) { mostCom = Math.max(mostCom,i.getPrice()); }
        for (Food i : foodHashMap.values()) { mostCom = Math.max(mostCom,i.getPrice()); }
        for (int i : workers) { mostCom = Math.max(mostCom,demo.getPriceOfAdventurer(i)); }
        System.out.println(mostCom);
    }

    public void checkComType(int id) {
        if (workers.contains(id)) {
            System.out.println("Commodity whose id is " + id + " belongs to Adventurer");
        }
        else if (foodHashMap.containsKey(id)) {
            System.out.println("Commodity whose id is " + id + " belongs to Food");
        }
        else if (bottleHashMap.containsKey(id)) {
            Bottle temBottle = bottleHashMap.get(id);
            if (temBottle instanceof RecoverBottle) {
                System.out.println("Commodity whose id is " + id + " belongs to RecoverBottle");
            }
            else if (temBottle instanceof ReinforcedBottle) {
                System.out.println("Commodity whose id is " + id + " belongs to ReinforcedBottle");
            }
            else {
                System.out.println("Commodity whose id is " + id + " belongs to RegularBottle");
            }
        }
        else if (equippmentHashMap.containsKey(id)) {
            Equipment temEquipment = equippmentHashMap.get(id);
            if (temEquipment instanceof EpicEquipment) {
                System.out.println("Commodity whose id is " + id + " belongs to EpicEquipment");
            }
            else if (temEquipment instanceof CritEquipment) {
                System.out.println("Commodity whose id is " + id + " belongs to CritEquipment");
            }
            else {
                System.out.println("Commodity whose id is " + id + " belongs to RegularEquipment");
            }
        }
    }

    public  void soldAll() {
        long sumPrice = money;
        sumPrice++;
        sumPrice--;
        ArrayList<Equipment> equipments = new ArrayList<>();
        equipments.addAll(bagForEquipment.values());
        for (Equipment i : equipments) {
            soldEquipment(i.getId(),false);
        }
        ArrayList<Bottle> bottless = new ArrayList<>();
        for (ArrayList<Bottle> bottles : bagForBottle.values()) {
            bottless.addAll(bottles);
        }
        for (Bottle i : bottless) {
            deleteBottle(i.getId(),false,true);
        }
        ArrayList<Food> foodss = new ArrayList<>();
        for (ArrayList<Food> foods : bagForFood.values()) {
            foodss.addAll(foods);
        }
        for (Food i : foodss) {
            soldFood(i.getId(),false);
        }
        sumPrice = money - sumPrice;
        System.out.println(name + " emptied the backpack " + sumPrice);
    }

    public void buy(int id,String name,String ty,double ratio,int critical) {
        if (ty.equals("Food")) {
            long price = shop.getSoldPrice("Food");
            if (price > money) { System.out.println("failed to buy " + name + " for " + price); }
            else {
                money -= price;
                this.addFood(id,name,shop.getSoldEnergy(),price);
                System.out.println("successfully buy " + name + " for " + price);
            }
        }
        else if (ty.equals("RegularBottle")) {
            long price = shop.getSoldPrice("Bottle");
            if (price > money) { System.out.println("failed to buy " + name + " for " + price); }
            else {
                money -= price;
                this.addBottle(id,name,shop.getSoldCapacity(),price,"RegularBottle",0);
                System.out.println("successfully buy " + name + " for " + price);
            }
        }
        else if (ty.equals("RecoverBottle")) {
            long price = shop.getSoldPrice("Bottle");
            if (price > money) { System.out.println("failed to buy " + name + " for " + price); }
            else { this.buyRecoverBottle(id,name,shop.getSoldCapacity(),price,ratio); }
        }
        else if (ty.equals("ReinforcedBottle")) {
            long price = shop.getSoldPrice("Bottle");
            if (price > money) { System.out.println("failed to buy " + name + " for " + price); }
            else { this.buyReinforcedBottle(id,name,shop.getSoldCapacity(),price,ratio); }
        }
        else if (ty.equals("RegularEquipment")) {
            long price = shop.getSoldPrice("Equipment");
            if (price > money) { System.out.println("failed to buy " + name + " for " + price); }
            else {
                money -= price;
                this.addEquipment(id,name,shop.getSoldStar(),price,1,1,"RegularEquipment");
                System.out.println("successfully buy " + name + " for " + price);
            }
        }
        else if (ty.equals("CritEquipment")) {
            long price = shop.getSoldPrice("Equipment");
            if (price > money) { System.out.println("failed to buy " + name + " for " + price); }
            else {
                money -= price;
                this.addEquipment(id,name,shop.getSoldStar(),price,critical,1,"CritEquipment");
                System.out.println("successfully buy " + name + " for " + price);
            }
        }
        else if (ty.equals("EpicEquipment")) {
            long price = shop.getSoldPrice("Equipment");
            if (price > money) { System.out.println("failed to buy " + name + " for " + price); }
            else {
                money -= price;
                this.addEquipment(id,name,shop.getSoldStar(),price,1,ratio,"EpicEquipment");
                System.out.println("successfully buy " + name + " for " + price);
            }
        }
    }

    public void buyRecoverBottle(int id,String name,int capacity,long price,double ratio) {
        money -= price;
        this.addBottle(id,name,capacity,price,"RecoverBottle",ratio);
        System.out.println("successfully buy " + name + " for " + price);
    }

    public void buyReinforcedBottle(int id,String name,int capacity,long price,double ratio) {
        money -= price;
        this.addBottle(id,name,capacity,price,"ReinforcedBottle",ratio);
        System.out.println("successfully buy " + name + " for " + price);
    }

    public void setSavedHitPoint() { savedHitPoint = hitpoint; }

    public int getSavedHitPoint() { return savedHitPoint; }

    public void askForHelp() {
        for (int id : workers) {
            money += demo.getHelpFromWorkers(id,savedHitPoint - hitpoint);
        }
    }

    public long provideHelp(int delta) {
        if (delta * 10000L <= money)
        {
            money -= delta * 10000L;
            return delta * 10000L;
        }
        else {
            long t = money;
            money = 0;
            return t;
        }
    }
}
