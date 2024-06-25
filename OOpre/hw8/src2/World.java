import java.util.ArrayList;
import java.util.HashMap;

public class World {
    private final HashMap<Integer, Adventurer> adventurers;
    // 保存一份使用name作为key的冒险者数据，加速战斗模式中查询
    private final HashMap<String, Adventurer> advDict;
    private HashMap<String, Adventurer> fightAdv;
    // 保存一份按照输入顺序排序的冒险者数据，加速一对多攻击中使用
    private ArrayList<Adventurer> fightAdvList;
    private ArrayList<Integer> fightAdvhitPointList;
    // 按照不同排序标准保存日志，加速日志的查询
    // Value部分使用ArrayList，保证了按照日志输入的顺序进行排列
    // 日期转转换成YYYYMM六位数作为Key
    private final HashMap<Integer, ArrayList<Log>> logByTime;
    private final HashMap<Integer, ArrayList<Log>> logByAttacker;
    private final HashMap<Integer, ArrayList<Log>> logByWounded;

    private static final World world = new World();

    private World() {
        this.adventurers = new HashMap<>();
        this.advDict = new HashMap<>();
        this.logByTime = new HashMap<>();
        this.logByAttacker = new HashMap<>();
        this.logByWounded = new HashMap<>();
    }

    public static World getInstance() {
        return world;
    }

    // 指令操作响应函数

    public boolean addAdv(int advId, String name) {
        Adventurer newAdv = new Adventurer(advId, name);
        adventurers.put(advId, newAdv);
        advDict.put(name, newAdv);
        return true;
    }

    public boolean addBotToAdv(int advId, int botId, String name, int capacity,
                               long price, String type, double ratio) {
        Adventurer changeAdv = adventurers.get(advId);
        return changeAdv.addBot(botId, name, capacity, price, type, ratio);
    }

    public boolean subBotOfAdv(int advId, int botId) {
        Adventurer changeAdv = adventurers.get(advId);
        String botName = changeAdv.getBotName(botId);
        boolean returnResult = changeAdv.subBot(botId);
        int botNum = changeAdv.getBotNum();
        System.out.println(botNum + " " + botName);
        return returnResult;
    }

    public boolean addEquToAdv(int advId, int equId, String name, int star,
                               long price, String type, int critical, double ratio) {
        Adventurer changeAdv = adventurers.get(advId);
        return changeAdv.addEqu(equId, name, star, price, type, critical, ratio);
    }

    public boolean subEquOfAdv(int advId, int equId) {
        Adventurer changeAdv = adventurers.get(advId);
        String equName = changeAdv.getEquName(equId);
        boolean returnResult = changeAdv.subEqu(equId);
        int equNum = changeAdv.getEquNum();
        System.out.println(equNum + " " + equName);
        return returnResult;
    }

    public boolean addStarOfEquOfAdv(int advId, int equId) {
        Adventurer changeAdv = adventurers.get(advId);
        String equName = changeAdv.getEquName(equId);
        boolean returnResult = changeAdv.addEquStar(equId);
        int equStar = changeAdv.getEquStar(equId);
        System.out.println(equName + " " + equStar);
        return returnResult;
    }

    public boolean addFoodToAdv(int advId, int foodId, String name, int energy,
                                long price) {
        Adventurer changeAdv = adventurers.get(advId);
        return changeAdv.addFood(foodId, name, energy, price);
    }

    public boolean subFoodOfAdv(int advId, int foodId) {
        Adventurer changeAdv = adventurers.get(advId);
        String foodName = changeAdv.getFoodName(foodId);
        boolean returnResult = changeAdv.subFood(foodId);
        int foodNum = changeAdv.getFoodNum();
        System.out.println(foodNum + " " + foodName);
        return returnResult;
    }

    public boolean advCarryEqu(int advId, int equId) {
        Adventurer changeAdv = adventurers.get(advId);
        return changeAdv.carryEqu(equId);
    }

    public boolean advCarryBot(int advId, int botId) {
        Adventurer changeAdv = adventurers.get(advId);
        return changeAdv.carryBot(botId);
    }

    public boolean advCarryFood(int advId, int foodId) {
        Adventurer changeAdv = adventurers.get(advId);
        return changeAdv.carryFood(foodId);
    }

    public boolean advUseBot(int advId, String name) {
        Adventurer changeAdv = adventurers.get(advId);
        int returnId = changeAdv.useBot(name);
        if (returnId != -1) {
            System.out.println(returnId + " " + changeAdv.getHitPoint());
            return true;
        } else {
            System.out.println("fail to use " + name);
            return false;
        }
    }

    public boolean advUseFood(int advId, String name) {
        Adventurer changeAdv = adventurers.get(advId);
        int returnId = changeAdv.useFood(name);
        if (returnId != -1) {
            System.out.println(returnId + " " + changeAdv.getLevel());
            return true;
        } else {
            System.out.println("fail to eat " + name);
            return false;
        }
    }

    public boolean addFightAdv(int m, ArrayList<String> advNames) {
        fightAdv = new HashMap<>();
        fightAdvList = new ArrayList<>();
        fightAdvhitPointList = new ArrayList<>();
        // 前三个数据分别为指令数、m和k，应该跳过，从下标为3处开始
        for (int i = 0; i < m; i++) {
            fightAdv.put(advNames.get(i + 3), advDict.get(advNames.get(i + 3)));
            fightAdvList.add(advDict.get(advNames.get(i + 3)));
            fightAdvhitPointList.add(advDict.get(advNames.get(i + 3)).getHitPoint());
        }
        System.out.println("Enter Fight Mode");
        return true;
    }

    public boolean queryLogByTime(int year, int month) {
        int timeKey = year * 100 + month;
        if (logByTime.containsKey(timeKey)) {
            for (Log log : logByTime.get(timeKey)) {
                System.out.println(log);
            }
            return true;
        } else {
            System.out.println("No Matched Log");
            return false;
        }
    }

    public boolean queryLogByAttacker(int advId) {
        if (logByAttacker.containsKey(advId)) {
            for (Log log : logByAttacker.get(advId)) {
                System.out.println(log);
            }
            return true;
        } else {
            System.out.println("No Matched Log");
            return false;
        }
    }

    public boolean queryLogByWounded(int advId) {
        if (logByWounded.containsKey(advId)) {
            for (Log log : logByWounded.get(advId)) {
                System.out.println(log);
            }
            return true;
        } else {
            System.out.println("No Matched Log");
            return false;
        }
    }

    public boolean advHireAdv(int advId1, int advId2) {
        Adventurer changeAdv = adventurers.get(advId1);
        Adventurer hiredAdv = adventurers.get(advId2);
        return changeAdv.hireAdv(hiredAdv);
    }

    public boolean queryAdvPrice(int advId) {
        Adventurer queryAdv = adventurers.get(advId);
        System.out.println(queryAdv.getPriceCount() + " " + queryAdv.getPrice());
        return true;
    }

    public boolean queryAdvPriceMax(int advId) {
        Adventurer queryAdv = adventurers.get(advId);
        System.out.println(queryAdv.getPriceMax());
        return true;
    }

    public boolean queryAdvComPrice(int advId, int comId) {
        Adventurer queryAdv = adventurers.get(advId);
        Commodity com = queryAdv.getComPrice(comId);
        System.out.print("Commodity whose id is " + comId + " belongs to ");
        System.out.println(com.getClass().getTypeName());
        return true;
    }

    // 日志操作响应函数

    public void addLogByTime(int key, Log log) {
        if (!logByTime.containsKey(key)) {
            logByTime.put(key, new ArrayList<>());
        }
        logByTime.get(key).add(log);
    }

    public void addLogByAttacker(int advId, Log log) {
        if (!logByAttacker.containsKey(advId)) {
            logByAttacker.put(advId, new ArrayList<>());
        }
        logByAttacker.get(advId).add(log);
    }

    public void addLogByWounded(int advId, Log log) {
        if (!logByWounded.containsKey(advId)) {
            logByWounded.put(advId, new ArrayList<>());
        }
        logByWounded.get(advId).add(log);
    }

    public boolean logType1(int year, int month, String advName1, String name) {
        if (fightAdv.containsKey(advName1)) {
            Adventurer changeAdv = fightAdv.get(advName1);
            int returnId = changeAdv.useBot(name);
            if (returnId != -1) {
                System.out.println(returnId + " " + changeAdv.getHitPoint());
                Log newLog = new Log(1, year, month, advName1, " ", name);
                addLogByTime(year * 100 + month, newLog);
                return true;
            } else {
                // 错误类型2：该药水未被携带
                System.out.println("Fight log error");
                return false;
            }
        } else {
            // 错误类型1：冒险者不处于战斗模式
            System.out.println("Fight log error");
            return false;
        }
    }

    public boolean logType2(int year, int month, String advName1, String advName2, String name) {
        if (fightAdv.containsKey(advName1) && fightAdv.containsKey(advName2)) {
            Adventurer attacker = fightAdv.get(advName1);
            Adventurer wounded = fightAdv.get(advName2);
            Equipment attackerEqu = attacker.useEqu(name);
            if (attackerEqu != null) {
                wounded.beAttacked(attackerEqu, attacker.getLevel());
                System.out.println(wounded.getId() + " " + wounded.getHitPoint());
                Log newLog = new Log(2, year, month, advName1, advName2, name);
                addLogByTime(year * 100 + month, newLog);
                addLogByAttacker(attacker.getId(), newLog);
                addLogByWounded(wounded.getId(), newLog);
                return true;
            } else {
                // 错误类型3：该武器未被携带
                System.out.println("Fight log error");
                return false;
            }
        } else {
            // 错误类型1：冒险者不处于战斗模式
            System.out.println("Fight log error");
            return false;
        }
    }

    public boolean logType3(int year, int month, String advName1, String name) {
        if (fightAdv.containsKey(advName1)) {
            Adventurer attacker = fightAdv.get(advName1);
            Equipment attackerEqu = attacker.useEqu(name);
            if (attackerEqu != null) {
                Log newLog = new Log(3, year, month, advName1, " ", name);
                addLogByTime(year * 100 + month, newLog);
                addLogByAttacker(attacker.getId(), newLog);
                for (Adventurer wounded : fightAdvList) {
                    // 不是自己才能打
                    if (wounded.getId() != attacker.getId()) {
                        wounded.beAttacked(attackerEqu, attacker.getLevel());
                        System.out.print(wounded.getHitPoint() + " ");
                        addLogByWounded(wounded.getId(), newLog);
                    }
                }
                System.out.print("\n");
                return true;
            } else {
                // 错误类型3：该武器未被携带
                System.out.println("Fight log error");
                return false;
            }
        } else {
            // 错误类型1：冒险者不处于战斗模式
            System.out.println("Fight log error");
            return false;
        }
    }

    public boolean advSellAll(int advId) {
        Adventurer changeAdv = adventurers.get(advId);
        long money = changeAdv.sellAll();
        System.out.println(changeAdv.getName() + " emptied the backpack " + money);
        return true;
    }

    public boolean advBuyFromStore(int advId,
                                   int id, String name, String type, int critical, double ratio) {
        Adventurer changAdv = adventurers.get(advId);
        long returnResult = changAdv.buyFromStore(id, name, type, critical, ratio);
        if (returnResult >= 0) {
            System.out.println("successfully buy " + name + " for " + returnResult);
        } else {
            System.out.println("failed to buy " + name + " for " + -returnResult);
        }
        return true;
    }

    public void exitFightMode() {
        for (int i = 0; i < fightAdvList.size(); i++) {
            if (fightAdvList.get(i).getHitPoint() <= (fightAdvhitPointList.get(i) / 2)) {
                fightAdvList.get(i).requireMoney(fightAdvhitPointList.get(i));
            }
        }
    }
}
