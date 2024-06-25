package util;

import config.GlobalConfig;
import config.GlobalScanner;
import constants.AbstractBaseTypeEnum;
import constants.FightLogTypeEnum;
import entity.Store;
import entity.adventure.Adventurer;
import entity.fightlog.FightLog;
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

import java.util.List;

public class OperatorUtil {

    @SuppressWarnings("checkstyle:Indentation")
    public static void matchChoice(String op) {
        String[] parts = op.split(" +");
        switch (Integer.parseInt(parts[0])) {
            case 1: addAdventurer(parts);
                break;
            case 2: addBottle(parts);
                break;
            case 3:
            case 5:
            case 8:
                sellProp(parts);
                break;
            case 4: addEquipment(parts);
                break;
            case 6: equStarPlus(parts);
                break;
            case 7: addFood(parts);
                break;
            case 9:
            case 10:
            case 11:
                carryProp(parts);
                break;
            case 12: useProp(parts, AbstractBaseTypeEnum.BOTTLE);
                break;
            case 13: useProp(parts, AbstractBaseTypeEnum.FOOD);
                break;
            case 14: enterFightMode(parts);
                break;
            case 15: searchFightLogByData(parts);
                break;
            case 16: searchFightLogByPosId(parts);
                break;
            case 17: searchFightLogByNegId(parts);
                break;
            case 18: hireAdventurer(parts);
                break;
            case 19: calcCommoditySum(parts);
                break;
            case 20: searchCommodityPriceMax(parts);
                break;
            case 21: searchClassNameByComId(parts);
                break;
            case 22: sellALlProp(parts);
                break;
            case 23: adventurerBugProp(parts);
                break;
            default:
                break;
        }
    }

    private static void addAdventurer(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        String name = parts[2];
        Adventurer adventurer = new Adventurer(advId, name);
        GlobalConfig.ADVENTURERS.addAdventurer(adventurer);
    }

    private static void addBottle(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        int botId = Integer.parseInt(parts[2]);
        String botName = parts[3];
        int botCap = Integer.parseInt(parts[4]);
        long botPrc = Long.parseLong(parts[5]);
        String botType = parts[6];
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        Prop bottle;
        if (botType.equals("RegularBottle")) {
            bottle = new RegularBottle(botId, botName, botCap, botPrc);
        } else if (botType.equals("ReinforcedBottle")) {
            double botRat = Double.parseDouble(parts[7]);
            bottle = new ReinforcedBottle(botId, botName, botCap, botPrc, botRat);
        } else {
            double botRat = Double.parseDouble(parts[7]);
            bottle = new RecoverBottle(botId, botName, botCap, botPrc, botRat);
        }
        adventurer.addProp(bottle);
    }

    private static void sellProp(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        int propId = Integer.parseInt(parts[2]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        Prop prop = adventurer.searchPropInPropsById(propId);
        adventurer.sellProp(prop);
        adventurer.delProp(prop);
        int reCount = adventurer.getProps().get(prop.getType()).size();
        System.out.println(reCount + " " + prop.getName());
    }

    private static void addEquipment(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        int equID = Integer.parseInt(parts[2]);
        String equName = parts[3];
        int star = Integer.parseInt(parts[4]);
        long equPrc = Long.parseLong(parts[5]);
        String equType = parts[6];
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        Prop equipment;
        if (equType.equals("RegularEquipment")) {
            equipment = new RegularEquipment(equID, equName, star, equPrc);
        } else if (equType.equals("CritEquipment")) {
            int critical = Integer.parseInt(parts[7]);
            equipment = new CritEquipment(equID, equName, star, equPrc, critical);
        } else {
            double ratio = Double.parseDouble(parts[7]);
            equipment = new EpicEquipment(equID, equName, star, equPrc, ratio);
        }
        adventurer.addProp(equipment);
    }

    private static void equStarPlus(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        int equId = Integer.parseInt(parts[2]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        Equipment equipment = (Equipment) adventurer.searchPropInPropsById(equId);
        equipment.equStarPlus();
        System.out.printf("%s %d\n", equipment.getName(), equipment.getStar());
    }

    private static void addFood(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        int foodId = Integer.parseInt(parts[2]);
        String foodName = parts[3];
        int foodEgy = Integer.parseInt(parts[4]);
        long foodPrc = Long.parseLong(parts[5]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        Prop prop = new Food(foodId, foodName, foodEgy, foodPrc);
        adventurer.addProp(prop);
    }

    private static void carryProp(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        int propId = Integer.parseInt(parts[2]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        Prop prop = adventurer.searchPropInPropsById(propId);
        adventurer.carryProp(prop);
    }

    private static void useProp(String[] parts, AbstractBaseTypeEnum type) {
        int advId = Integer.parseInt(parts[1]);
        String propName = parts[2];
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        Prop prop = adventurer.useProp(propName, type);
        if (prop == null) {
            if (adventurer.isFightMode()) {
                System.out.println("Fight log error");
            } else {
                if (type.equals(AbstractBaseTypeEnum.BOTTLE)) {
                    System.out.println("fail to use " + propName);
                } else if (type.equals(AbstractBaseTypeEnum.FOOD)) {
                    System.out.println("fail to eat " + propName);
                }
            }
        } else {
            if (prop instanceof Bottle) {
                System.out.printf("%d %d\n", prop.getId(), adventurer.getHitPoint());
            } else if (prop instanceof Food) {
                System.out.printf("%d %d\n", prop.getId(), adventurer.getLevel());
            }
        }
    }

    private static void enterFightMode(String[] parts) {
        int fightPeopleCount = Integer.parseInt(parts[1]);
        int fightLogCount = Integer.parseInt(parts[2]);
        String[] adventurerInFightMode = new String[fightPeopleCount];
        for (int i = 0; i < fightPeopleCount; i++) {
            String name = parts[i + 3];
            Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerByName(name);
            adventurer.enterFightMode();
            adventurerInFightMode[i] = name;
        }
        System.out.println("Enter Fight Mode");
        for (int i = 0; i < fightLogCount; i++) {
            String fightLog = GlobalScanner.SCANNER.nextLine();
            FightLogParser.processFightLog(fightLog, adventurerInFightMode);
        }
        for (int i = 0; i < fightPeopleCount; i++) {
            String name = parts[i + 3];
            Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerByName(name);
            adventurer.quitFightMode();
        }
    }

    private static void searchFightLogByData(String[] parts) {
        String date = parts[1];
        List<FightLog> logs = GlobalConfig.FIGHT_LOGS.getFightLogByDate(date);
        if (logs == null) {
            System.out.println("No Matched Log");
            return;
        }
        for (FightLog log : logs) {
            if (log.getType().equals(FightLogTypeEnum.AOE_ATTACK) && !log.getIsCompleteAoe()) {
                continue;
            }
            System.out.println(log);
        }
    }

    private static void searchFightLogByPosId(String[] parts) {
        int posId = Integer.parseInt(parts[1]);
        List<FightLog> logs = GlobalConfig.FIGHT_LOGS.getFightLogsByPosId(posId);
        if (logs == null) {
            System.out.println("No Matched Log");
            return;
        }
        for (FightLog log : logs) {
            if (log.getType() == FightLogTypeEnum.SINGLE_ATTACK) {
                System.out.println(log);
            } else if (log.getType() == FightLogTypeEnum.AOE_ATTACK && log.getIsCompleteAoe()) {
                System.out.println(log);
            }
        }
    }

    private static void searchFightLogByNegId(String[] parts) {
        int negId = Integer.parseInt(parts[1]);
        List<FightLog> logs = GlobalConfig.FIGHT_LOGS.getFightLogsByNegId(negId);
        if (logs == null) {
            System.out.println("No Matched Log");
            return;
        }
        for (FightLog log : logs) {
            if (log.getType() == FightLogTypeEnum.SINGLE_ATTACK) {
                System.out.println(log);
            } else if (log.getType() == FightLogTypeEnum.AOE_ATTACK && !log.getIsCompleteAoe()) {
                System.out.println(log);
            }
        }
    }

    private static void hireAdventurer(String[] parts) {
        int employerId = Integer.parseInt(parts[1]);
        int employeeId = Integer.parseInt(parts[2]);
        Adventurer employer = GlobalConfig.ADVENTURERS.getAdventurerById(employerId);
        Adventurer employee = GlobalConfig.ADVENTURERS.getAdventurerById(employeeId);
        employer.hireAdv(employee);
    }

    private static void calcCommoditySum(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        long advComPrc = adventurer.getComPrice();
        long advMoney = adventurer.getMoney();
        int advComNum = adventurer.getComNum();
        System.out.println(advComNum + " " + (advComPrc - advMoney));
    }

    private static void searchCommodityPriceMax(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        long maxComPrice = adventurer.getMaxComPrice();
        System.out.println(maxComPrice);
    }

    private static void searchClassNameByComId(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        int comId = Integer.parseInt(parts[2]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        String className = adventurer.searchComById(comId);
        System.out.println("Commodity whose id is " + comId + " belongs to " + className);
    }

    private static void sellALlProp(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        long initMoney = adventurer.getMoney();
        adventurer.sellAllProp();
        long finalMoney = adventurer.getMoney();
        long revenue = finalMoney - initMoney;
        System.out.println(adventurer.getName() + " emptied the backpack " + revenue);
    }

    private static void adventurerBugProp(String[] parts) {
        int advId = Integer.parseInt(parts[1]);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerById(advId);
        int propId = Integer.parseInt(parts[2]);
        String propName = parts[3];
        String propType = parts[4];
        int critical;
        double ratio;
        if (propType.equals("CritEquipment")) {
            critical = Integer.parseInt(parts[5]);
            ratio = 0;
        } else if (propType.equals("Food") || propType.equals("RegularBottle")
                                           || propType.equals("RegularEquipment")) {
            critical = 0;
            ratio = 0;
        } else {
            critical = 0;
            ratio = Double.parseDouble(parts[5]);
        }
        Store.getInstance().sellGoodsTo(adventurer, propId, propName, propType, ratio, critical);
    }
}
