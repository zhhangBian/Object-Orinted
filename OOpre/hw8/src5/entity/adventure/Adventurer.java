package entity.adventure;

import constants.AbstractBaseTypeEnum;
import entity.Commodity;
import entity.Store;
import entity.prop.Prop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Adventurer implements Commodity {

    private final int id;

    private final String name;

    private int hitPoint;

    private int level;

    private boolean isFightMode;

    private long money;

    private Adventurer attackTarget;

    private int initHitPoint;

    private final Map<AbstractBaseTypeEnum, Map<Integer, Prop>> props;

    private final Map<AbstractBaseTypeEnum, Map<String, TreeSet<Prop>>> bag;

    private final Set<Adventurer> hireAdventurers;

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
        this.hitPoint = 500;
        this.level = 1;
        this.isFightMode = false;
        this.props = new HashMap<>();
        this.bag = new HashMap<>();
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            Map<Integer, Prop> hashMap1 = new HashMap<>();
            props.put(type, hashMap1);
            Map<String, TreeSet<Prop>> hashMap2 = new HashMap<>();
            bag.put(type, hashMap2);
        }
        this.hireAdventurers = new HashSet<>();
        this.money = 0;
    }

    //propOperate

    public void addProp(Prop prop) {
        this.props.get(prop.getType()).put(prop.getId(), prop);
    }

    public void delProp(Prop prop) {
        this.props.get(prop.getType()).remove(prop.getId(), prop);
        if (bag.get(prop.getType()).containsKey(prop.getName())) {
            bag.get(prop.getType()).get(prop.getName()).remove(prop);
        }
    }

    public void sellProp(Prop prop) {
        long pay = Store.getInstance().buyGoods(prop);
        this.money += pay;
    }

    public void sellAllProp() {
        List<Prop> removeProp = new ArrayList<>();
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            HashMap<String, TreeSet<Prop>> hashMap = (HashMap<String, TreeSet<Prop>>) bag.get(type);
            for (TreeSet<Prop> treeSet : hashMap.values()) {
                removeProp.addAll(treeSet);
            }
        }
        for (Prop prop : removeProp) {
            sellProp(prop);
            delProp(prop);
        }
    }

    public void carryProp(Prop prop) {
        prop.carryBy(this);
    }

    public Prop useProp(String name, AbstractBaseTypeEnum type) {
        Prop prop = searchPropInBagByName(name, type);
        if (prop == null) {
            return null;
        }
        prop.useBy(this);
        return prop;
    }

    public Prop searchPropInBagByName(String name, AbstractBaseTypeEnum type) {
        Map<String, TreeSet<Prop>> propBag = bag.get(type);
        if (!propBag.containsKey(name) || propBag.get(name).isEmpty()) {
            return null;
        }
        return propBag.get(name).first();
    }

    public Prop searchPropInPropsById(int id) {
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            if (props.get(type).containsKey(id)) {
                return props.get(type).get(id);
            }
        }
        return null;
    }

    //fightOperate

    public void enterFightMode() {
        this.isFightMode = true;
        this.initHitPoint = this.hitPoint;
    }

    public void quitFightMode() {
        this.isFightMode = false;
        if (hitPoint <= (initHitPoint / 2)) {
            long decreaseHitPoint = initHitPoint - hitPoint;
            for (Adventurer adventurer : hireAdventurers) {
                this.money += adventurer.aidEmployer(decreaseHitPoint);
            }
        }
    }

    public void attacked(int healthPointDecrease) {
        this.hitPoint -= healthPointDecrease;
    }

    public void fight(Adventurer attacked, Prop prop) {
        this.attackTarget = attacked;
        useProp(prop.getName(), AbstractBaseTypeEnum.EQUIPMENT);
    }

    public void hireAdv(Adventurer adventurer) {
        this.hireAdventurers.add(adventurer);
    }

    //CommodityOperate

    @Override
    public long getComPrice() {
        long priceSum = 0;
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            for (Commodity commodity : props.get(type).values()) {
                priceSum += commodity.getComPrice();
            }
        }
        for (Adventurer adventurer : hireAdventurers) {
            priceSum += adventurer.getComPrice();
        }
        return priceSum + this.money;
    }

    @Override
    public int getComNum() {
        int num = 0;
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            for (Commodity commodity : props.get(type).values()) {
                num += commodity.getComNum();
            }
        }
        return num + hireAdventurers.size();
    }

    public long getMaxComPrice() {
        long maxComPrice = 0;
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            for (Commodity commodity : props.get(type).values()) {
                maxComPrice = Math.max(maxComPrice, commodity.getComPrice());
            }
        }
        for (Adventurer adventurer : hireAdventurers) {
            maxComPrice = Math.max(maxComPrice, adventurer.getComPrice());
        }
        return maxComPrice;
    }

    public String searchComById(int comId) {
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            for (Prop commodity : props.get(type).values()) {
                if (commodity.getId() == comId) {
                    return commodity.getClass().getSimpleName();
                }
            }
        }
        for (Adventurer adventurer : hireAdventurers) {
            if (adventurer.getId() == comId) {
                return "Adventurer";
            }
        }
        return "not found";
    }

    public long aidEmployer(long decreaseHitPoint) {
        if (this.money >= decreaseHitPoint * 10000) {
            this.money -= decreaseHitPoint * 10000;
            return decreaseHitPoint * 10000;
        } else {
            long aidMoney = this.money;
            this.money = 0;
            return aidMoney;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public int getLevel() {
        return level;
    }

    public boolean isFightMode() {
        return isFightMode;
    }

    public long getMoney() {
        return money;
    }

    public Map<AbstractBaseTypeEnum, Map<Integer, Prop>> getProps() {
        return props;
    }

    public Map<AbstractBaseTypeEnum, Map<String, TreeSet<Prop>>> getBag() {
        return bag;
    }

    public Adventurer getAttackTarget() {
        return attackTarget;
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
