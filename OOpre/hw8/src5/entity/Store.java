package entity;

import config.AttributeMap;
import constants.AbstractBaseTypeEnum;
import entity.adventure.Adventurer;
import entity.prop.Prop;
import entity.prop.bottle.Bottle;
import entity.prop.bottle.RecoverBottle;
import entity.prop.bottle.RegularBottle;
import entity.prop.bottle.ReinforcedBottle;
import entity.prop.equipment.CritEquipment;
import entity.prop.equipment.EpicEquipment;
import entity.prop.equipment.Equipment;
import entity.prop.equipment.RegularEquipment;
import entity.prop.food.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {

    private static Store store;

    private final Map<AbstractBaseTypeEnum, List<Prop>> storeHouse;

    private Store() {
        storeHouse = new HashMap<>();
        AbstractBaseTypeEnum[] enums = AbstractBaseTypeEnum.values();
        for (AbstractBaseTypeEnum type : enums) {
            List<Prop> list = new ArrayList<>();
            storeHouse.put(type, list);
        }
    }

    public static Store getInstance() {
        if (store == null) {
            store = new Store();
        }
        return store;
    }

    public long buyGoods(Prop goods) {
        storeHouse.get(goods.getType()).add(goods);
        return goods.getPrice();
    }

    public void sellGoodsTo(Adventurer adventurer, int id, String name, String type,
                            double ratio, int critical) {
        long tradePrice = getTradePrice(type);
        if (adventurer.getMoney() >= tradePrice) {
            adventurer.setMoney((adventurer.getMoney() - tradePrice));
            Prop prop = creatProp(id, name, type, tradePrice, ratio, critical);
            adventurer.addProp(prop);
            System.out.println("successfully buy " + name + " for " + tradePrice);
        } else {
            System.out.println("failed to buy " + name + " for " + tradePrice);
        }
    }

    public long getTradePrice(String type) {
        AbstractBaseTypeEnum abstractBaseTypeEnum = AttributeMap.ATTRIBUTE_MAP.get(type);
        List<Prop> list = this.storeHouse.get(abstractBaseTypeEnum);
        long sumPrice = 0;
        if (list == null) {
            return 0;
        }
        for (Prop prop : list) {
            sumPrice += prop.getPrice();
        }
        return sumPrice / list.size();
    }

    public int getGoodsBaseAttribute(String type) {
        AbstractBaseTypeEnum abstractBaseTypeEnum = AttributeMap.ATTRIBUTE_MAP.get(type);
        List<Prop> list = this.storeHouse.get(abstractBaseTypeEnum);
        if (list == null) {
            return 0;
        }
        if (abstractBaseTypeEnum.equals(AbstractBaseTypeEnum.FOOD)) {
            int sumEnergy = 0;
            for (Prop prop: list) {
                Food food = (Food) prop;
                sumEnergy += food.getEnergy();
            }
            return sumEnergy / list.size();
        } else if (abstractBaseTypeEnum.equals(AbstractBaseTypeEnum.BOTTLE)) {
            int sumCapacity = 0;
            for (Prop prop: list) {
                Bottle bottle = (Bottle) prop;
                sumCapacity += bottle.getCapacity();
            }
            return sumCapacity / list.size();
        } else {
            int sumStar = 0;
            for (Prop prop: list) {
                Equipment equipment = (Equipment) prop;
                sumStar += equipment.getStar();
            }
            return sumStar / list.size();
        }
    }

    public Prop creatProp(int id, String name, String type, long price,
                          double ratio, int critical) {
        switch (type) {
            case "Food":
                return new Food(id, name, getGoodsBaseAttribute(type), price);
            case "RegularEquipment":
                return new RegularEquipment(id, name, getGoodsBaseAttribute(type), price);
            case "RegularBottle":
                return new RegularBottle(id, name, getGoodsBaseAttribute(type), price);
            case "CritEquipment":
                return new CritEquipment(id, name, getGoodsBaseAttribute(type), price, critical);
            case "EpicEquipment":
                return new EpicEquipment(id, name, getGoodsBaseAttribute(type), price, ratio);
            case "ReinforcedBottle":
                return new ReinforcedBottle(id, name, getGoodsBaseAttribute(type), price, ratio);
            case "RecoverBottle":
                return new RecoverBottle(id, name, getGoodsBaseAttribute(type), price, ratio);
            default:
                return null;
        }
    }

}