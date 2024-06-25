import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Adventure implements Commodity {
    private int id;
    private String name;
    private HashMap<Integer,Equipment> equipments;
    private HashMap<Integer,Bottle> bottles;
    private HashMap<Integer,Food> foods;
    private HashMap<Integer,Adventure> hire;
    private int hitPoint;                 //体力初始为500
    private int level;                    //等级，初始为1
    private Boolean inBattle;
    private AdvLog advLog;
    private Long money;

    public Adventure(int id,String name) {                                            //初始体力和等级
        this.id = id;
        this.name = name;
        bottles = new HashMap<>();
        equipments = new HashMap<>();
        hire = new HashMap<>();
        foods = new HashMap<>();
        hitPoint = 500;
        level = 1;
        inBattle = false;
        advLog = new AdvLog();
        money = 0L;
    }

    public void help(int a) {
        for (int key : hire.keySet()) {
            if (hire.get(key).getMoney() >= (a * 10000L)) {
                hire.get(key).subMoney(a * 10000L);
                money += a * 10000L;
            } else {
                money += hire.get(key).getMoney();
                hire.get(key).noMoney();
            }
        }
    }

    public void hireAdventure(Adventure adventure) {
        hire.put(adventure.getId(),adventure);
    }

    public void getAdvPrice() {
        int value = 0;
        if (!equipments.isEmpty()) {
            for (int key : equipments.keySet()) { value++; }
        } if (!bottles.isEmpty()) {
            for (int key : bottles.keySet()) { value++; }
        } if (!foods.isEmpty()) {
            for (int key : foods.keySet()) { value++; }
        } if (!hire.isEmpty()) {
            for (int key : hire.keySet()) { value++; }
        }
        System.out.println(value + " " + getPrice());
    }

    public void sellAll() {
        Shop shop = Shop.getInstance();
        long totalMoney = 0L;
        Iterator<Map.Entry<Integer, Food>> iterator = foods.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Food> entry = iterator.next();
            Integer key = entry.getKey();
            Food food = entry.getValue();
            if (food.isInBackpack()) {
                totalMoney += food.getPrice();
                shop.sellItem("food",food.getEnergy(),food.getPrice());
                iterator.remove();
            }
        }
        Iterator<Map.Entry<Integer, Bottle>> iterator1 = bottles.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<Integer, Bottle> entry = iterator1.next();
            Integer key = entry.getKey();
            Bottle bottle = entry.getValue();
            if (bottle.isInBackpack()) {
                totalMoney += bottle.getPrice();
                shop.sellItem("bottle",bottle.getCapacity(),bottle.getPrice());
                iterator1.remove();
            }
        }
        Iterator<Map.Entry<Integer, Equipment>> iterator2 = equipments.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<Integer, Equipment> entry = iterator2.next();
            Integer key = entry.getKey();
            Equipment equipment = entry.getValue();
            if (equipment.isInBackpack()) {
                totalMoney += equipment.getPrice();
                shop.sellItem("equipment",equipment.getStar(),equipment.getPrice());
                iterator2.remove();
            }
        }
        System.out.println(name + " emptied the backpack " + totalMoney);
        money += totalMoney;
    }

    public void buyFromShop(ArrayList<String> strings) {
        Shop shop = Shop.getInstance();
        int id = Integer.parseInt(strings.get(2));
        String name = strings.get(3);
        String type = strings.get(4);
        long price = shop.getItemPrice(type);
        if (money < price) {
            System.out.println("failed to buy " + name + " for " + price);
        }
        else {
            System.out.println("successfully buy " + name + " for " + price);
            money -= price;
            if (type.equals("RegularBottle")) {
                RegularBottle bottle = new RegularBottle(id,name,shop.getAttribute(type),price);
                bottles.put(id,bottle);
            } else if (type.equals("RecoverBottle")) {
                Double r = Double.parseDouble(strings.get(5));
                RecoverBottle bottle = new RecoverBottle(id,name,shop.getAttribute(type),price,r);
                bottles.put(id,bottle);
            } else if (type.equals("ReinforcedBottle")) {
                Double r = Double.parseDouble(strings.get(5));
                ReinforcedBottle b = new ReinforcedBottle(id,name,shop.getAttribute(type),price,r);
                bottles.put(id,b);
            } else if (type.equals("RegularEquipment")) {
                RegularEquipment e = new RegularEquipment(id,name,shop.getAttribute(type),price);
                equipments.put(id,e);
            } else if (type.equals("CritEquipment")) {
                int crit = Integer.parseInt(strings.get(5));
                CritEquipment e = new CritEquipment(id,name,shop.getAttribute(type),price,crit);
                equipments.put(id,e);
            } else if (type.equals("EpicEquipment")) {
                Double r = Double.parseDouble(strings.get(5));
                EpicEquipment e = new EpicEquipment(id,name,shop.getAttribute(type),price,r);
                equipments.put(id,e);
            } else {
                Food food = new Food(id,name,shop.getAttribute(type),price);
                foods.put(id,food);
            }
        }
    }

    public Long getPrice() {
        Long totalValue = 0L;
        if (!equipments.isEmpty()) {
            for (int key : equipments.keySet()) {
                totalValue += equipments.get(key).getPrice();
            }
        } if (!bottles.isEmpty()) {
            for (int key : bottles.keySet()) {
                totalValue += bottles.get(key).getPrice();
            }
        } if (!foods.isEmpty()) {
            for (int key : foods.keySet()) {
                totalValue += foods.get(key).getPrice();
            }
        } if (!hire.isEmpty()) {
            for (int key : hire.keySet()) {
                totalValue += hire.get(key).getPrice();
                totalValue += hire.get(key).getMoney();
            }
        }
        return totalValue;
    }

    public void getLargestPrice() {
        Long price = 0L;
        if (!equipments.isEmpty()) {
            for (int key : equipments.keySet()) {
                if (equipments.get(key).getPrice() > price) {
                    price = equipments.get(key).getPrice();
                }
            }
        }
        if (!bottles.isEmpty()) {
            for (int key : bottles.keySet()) {
                if (bottles.get(key).getPrice() > price) {
                    price = bottles.get(key).getPrice();
                }
            }
        }
        if (!foods.isEmpty()) {
            for (int key : foods.keySet()) {
                if (foods.get(key).getPrice() > price) {
                    price = foods.get(key).getPrice();
                }
            }
        }
        if (!hire.isEmpty()) {
            for (int key : hire.keySet()) {
                if (hire.get(key).getPrice() + hire.get(key).getMoney() > price) {
                    price = hire.get(key).getPrice() + hire.get(key).getMoney();
                }
            }
        }
        System.out.println(price);
    }

    public void getTypeName(int id) {
        if (!equipments.isEmpty()) {
            for (int key : equipments.keySet()) {
                if (equipments.get(key).getId() == id) {
                    System.out.println("Commodity whose id is " + id
                            + " belongs to " + equipments.get(key).getType());
                }
            }
        }
        if (!bottles.isEmpty()) {
            for (int key : bottles.keySet()) {
                if (bottles.get(key).getId() == id) {
                    System.out.println("Commodity whose id is " + id
                            + " belongs to " + bottles.get(key).getType());
                }
            }
        }
        if (!foods.isEmpty()) {
            for (int key : foods.keySet()) {
                if (foods.get(key).getId() == id) {
                    System.out.println("Commodity whose id is " + id
                            + " belongs to Food");
                }
            }
        }
        if (!hire.isEmpty()) {
            for (int key : hire.keySet()) {
                if (hire.get(key).getId() == id) {
                    System.out.println("Commodity whose id is " + id
                            + " belongs to Adventurer");
                }
            }
        }
    }

    public void addBottle(ArrayList<String> string) { //增加一个药水瓶
        int id = Integer.parseInt(string.get(2));
        String name = string.get(3);
        int capacity = Integer.parseInt(string.get(4));
        long price = Long.parseLong(string.get(5));
        String type = string.get(6);
        if (type.equals("RegularBottle")) {
            RegularBottle bottle = new RegularBottle(id,name,capacity,price);
            bottles.put(id,bottle);
        }
        else {
            Double ratio = Double.parseDouble(string.get(7));
            if (type.equals("ReinforcedBottle")) {
                ReinforcedBottle bottle = new ReinforcedBottle(id,name,capacity,price,ratio);
                bottles.put(id,bottle);
            }
            else {
                RecoverBottle bottle = new RecoverBottle(id,name,capacity,price,ratio);
                bottles.put(id,bottle);
            }
        }
    }

    public void deleteBottle(String string) {
        Shop shop = Shop.getInstance();
        int id = Integer.parseInt(string);
        System.out.println(bottles.size() - 1 + " " + bottles.get(id).getName());     //删除某个药水瓶
        shop.sellItem("bottle",bottles.get(id).getCapacity(),bottles.get(id).getPrice());
        money += bottles.get(id).getPrice();
        bottles.remove(id);
    }

    public int getMaxBottles() {
        return getLevel() / 5 + 1;
    }

    public  void takeBottle(String string) {
        int id = Integer.parseInt(string);
        int counter = 0;
        for (int key:bottles.keySet()) {
            if (key != id) {
                if (bottles.get(id).getName().equals(bottles.get(key).getName())) {
                    if (bottles.get(key).isInBackpack()) {
                        counter += 1;
                    }
                }
            }
        }
        if (counter < getMaxBottles()) {             //limited about taking bottle
            bottles.get(id).takeOn();
        }
    }

    public Boolean useBottle(String name) {
        int id = 2147483647;
        for (int key:bottles.keySet()) {
            if (bottles.get(key).isInBackpack()) {
                if (bottles.get(key).getName().equals(name)) {
                    if (key < id) {
                        id = key;
                    }
                }
            }
        }
        if (id < 2147483647) {
            if (bottles.get(id).isEmpty()) {
                bottles.remove(id);
            }
            else {
                addHitPoint(bottles.get(id).getPoint(hitPoint));
                bottles.get(id).emptyBottle();
            }
            System.out.println(id + " " + getHitPoint());
            return true;
        }
        else if (getInBattle()) {
            return false;
        }
        else {
            System.out.println("fail to use " + name);
            return false;
        }
    }

    public void addEquipment(ArrayList<String> strings) { //添加一个装备
        int id = Integer.parseInt(strings.get(2));
        String name = strings.get(3);
        int star = Integer.parseInt(strings.get(4));
        Long price = Long.parseLong(strings.get(5));
        String type = strings.get(6);
        if (type.equals("RegularEquipment")) {
            RegularEquipment equipment = new RegularEquipment(id, name, star,price);
            equipments.put(id, equipment);
        }
        else if (type.equals("CritEquipment")) {
            int critical = Integer.parseInt(strings.get(7));
            CritEquipment equipment = new CritEquipment(id,name,star,price,critical);
            equipments.put(id,equipment);
        }
        else {
            Double ratio = Double.parseDouble(strings.get(7));
            EpicEquipment equipment = new EpicEquipment(id,name,star,price,ratio);
            equipments.put(id,equipment);
        }
    }

    public void deleteEquipment(String string) { //删除某个装备
        Shop shop = Shop.getInstance();
        int id = Integer.parseInt(string);
        System.out.println(equipments.size() - 1 + " " + equipments.get(id).getName());
        shop.sellItem("equipment",equipments.get(id).getStar(),equipments.get(id).getPrice());
        money += equipments.get(id).getPrice();
        equipments.remove(id);
    }

    public void raiseEquipment(String string) { //给某个装备提升一个星级
        int id = Integer.parseInt(string);
        equipments.get(id).addStar();
        System.out.println(equipments.get(id).getName() + " " + equipments.get(id).getStar());
    }

    public  void takeEquipment(String string) {
        int id = Integer.parseInt(string);
        for (int key:equipments.keySet()) {
            if (key != id) {
                if (equipments.get(id).getName().equals(equipments.get(key).getName())) {
                    if (equipments.get(key).isInBackpack()) {
                        equipments.get(key).takeOff();
                        break;
                    }
                }
            }
        }
        equipments.get(id).takeOn();
    }

    public HashMap<Integer, Equipment> getEquipments() {
        return equipments;
    }

    public void addFood(ArrayList<String> string) {
        int id = Integer.parseInt(string.get(2));
        String name = string.get(3);
        int energy = Integer.parseInt(string.get(4));
        Long price = Long.parseLong(string.get(5));
        Food food = new Food(id,name,energy,price);
        foods.put(id,food);
    }

    public void deleteFood(String string) {
        Shop shop = Shop.getInstance();
        int id = Integer.parseInt(string);
        System.out.println(foods.size() - 1 + " " + foods.get(id).getName());
        shop.sellItem("food",foods.get(id).getEnergy(),foods.get(id).getPrice());
        money += foods.get(id).getPrice();
        foods.remove(id);
    }

    public void takeFood(String string) {
        int id = Integer.parseInt(string);
        for (int key:foods.keySet()) {
            if (key == id) {
                foods.get(key).takeOn();
            }
        }
    }

    public void useFood(String name) {
        int id = 2147483647;
        for (int key:foods.keySet()) {
            if (foods.get(key).isInBackpack()) {
                if (foods.get(key).getName().equals(name)) {
                    if (foods.get(key).getId() < id) {
                        id = foods.get(key).getId();
                    }
                }
            }
        }
        if (id < 2147483647) {
            addLevel(foods.get(id).getEnergy());
            System.out.println(id + " " + getLevel());
            foods.remove(id);
        }
        else {
            System.out.println("fail to eat " + name);
        }
    }

    public void raiseLevel(int num) {
        level += num;
    }

    public int getId() {
        return id;
    }                                                   //获取冒险者id

    public void addHitPoint(int num) {
        hitPoint += num;
    }

    public void addLevel(int num) {
        level += num;
    }

    public void minusLevel(int num) {
        level -= num;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public int getLevel() {
        return level;
    }

    public int bottleNum() {
        return bottles.size();
    }

    public int equipmentNum() {
        return equipments.size();
    }

    public int foodNum() {
        return foods.size();
    }

    public String getName() {
        return name;
    }

    public void HitPointDecrease(Equipment equipment,Adventure adventure) {
        hitPoint -= equipment.getPoint(adventure,hitPoint);
    }

    public void joinBattle() {
        inBattle = true;
    }

    public void dropBattle() {
        inBattle = false;
    }

    public Boolean getInBattle() {
        return inBattle;
    }

    public AdvLog getAdvLog() {
        return advLog;
    }

    public Long getMoney() {
        return money;
    }

    public void subMoney(long a) {
        money -= a;
    }

    public void noMoney() {
        money = 0L;
    }
}


