import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;

public class Demo {
    private final Scanner scanner;
    private HashMap<Integer,Adventurer> adventurerHashMap;
    private FightLog fightLog;

    public Demo(Scanner scanner) {
        this.scanner = scanner;
        adventurerHashMap = new HashMap<>();
        fightLog = new FightLog();
    }

    public void solve() {
        int num = scanner.nextInt();
        for (int i = 1;i <= num;i++) {
            int type = scanner.nextInt();
            if (type == 1) { this.addAdventure(); }
            else if (type == 2) { this.addBottleToAdventurer(); }
            else if (type == 3) { this.adventurerSoldBottle(); }
            else if (type == 4) { this.addEquipmentToAdventurer(); }
            else if (type == 5) { this.adventurerSoldEquipment(); }
            else if (type == 6) { this.addAStar(); }
            else if (type == 7) { this.addFoodToAdventurer(); }
            else if (type == 8) { this.adventurerSoldFood(); }
            else if (type == 9) { this.tryCarryEquipment(); }
            else if (type == 10) { this.tryCarryBottle(); }
            else if (type == 11) { this.tryCarryFood(); }
            else if (type == 12) { this.tryUseBottle(); }
            else if (type == 13) { this.tryUseFood(); }
            else if (type == 14) { this.recordLog(); }
            else if (type == 15) { this.checkByDate(); }
            else if (type == 16) { this.checkHit(); }
            else if (type == 17) { this.checkHitBy(); }
            else if (type == 18) { this.tryToPay(); }
            else if (type == 19) { this.checkPrice(); }
            else if (type == 20) { this.checkMost(); }
            else if (type == 21) { this.checkType(); }
            else if (type == 22) { this.adventurerSoldAll(); }
            else if (type == 23) { this.adventurerBuy(); }
        }
    }

    public void addAdventure() {
        int id = scanner.nextInt();
        String name = scanner.next();
        Adventurer newadventurer = new Adventurer(id,name,this);
        adventurerHashMap.put(id,newadventurer);
    }

    public void addBottleToAdventurer() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idB = scanner.nextInt();
        String name = scanner.next();
        int capacity = scanner.nextInt();
        long price = scanner.nextLong();
        String type = scanner.next();
        if (type.equals("RegularBottle")) {
            temadventurer.addBottle(idB, name, capacity,price,type,1);
        }
        else if (type.equals("ReinforcedBottle")) {
            double ratio = scanner.nextDouble();
            temadventurer.addBottle(idB, name, capacity,price,type,ratio);
        }
        else if (type.equals("RecoverBottle")) {
            double ratio = scanner.nextDouble();
            temadventurer.addBottle(idB, name, capacity,price,type,ratio);
        }
    }

    public void deleteBottleOfAdventurer() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idB = scanner.nextInt();
        temadventurer.deleteBottle(idB,true,true);
    }

    public void adventurerSoldBottle() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idB = scanner.nextInt();
        temadventurer.deleteBottle(idB,true,true);
    }

    public void tryCarryBottle() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idB = scanner.nextInt();
        temadventurer.carryBottle(idB);
    }

    public void tryUseBottle() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        String name = scanner.next();
        temadventurer.useBottle(name,true);
    }

    public void addEquipmentToAdventurer() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idE = scanner.nextInt();
        String name = scanner.next();
        int star = scanner.nextInt();
        long price = scanner.nextLong();
        String type = scanner.next();
        if (type.equals("RegularEquipment")) {
            temadventurer.addEquipment(idE, name, star, price,0,0,type);
        }
        else if (type.equals("CritEquipment")) {
            int critical = scanner.nextInt();
            temadventurer.addEquipment(idE, name, star, price,critical,0,type);
        }
        else if (type.equals("EpicEquipment")) {
            double ratio = scanner.nextDouble();
            temadventurer.addEquipment(idE, name, star, price,0,ratio,type);
        }
    }

    public void deleteEquipmentOfAdventurer() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idE = scanner.nextInt();
        //temadventurer.deleteEquipment(idE);
    }

    public  void adventurerSoldEquipment() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idE = scanner.nextInt();
        temadventurer.soldEquipment(idE,true);
    }

    public void addAStar() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idE = scanner.nextInt();
        temadventurer.addStarOfEquipment(idE);
    }

    public void tryCarryEquipment() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idE = scanner.nextInt();
        temadventurer.carryEquipment(idE);
    }

    public void addFoodToAdventurer() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idF = scanner.nextInt();
        String name = scanner.next();
        int energy = scanner.nextInt();
        long price = scanner.nextLong();
        temadventurer.addFood(idF,name,energy,price);
    }

    public  void  adventurerSoldFood() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idF = scanner.nextInt();
        temadventurer.soldFood(idF,true);
    }

    public void tryCarryFood() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idF = scanner.nextInt();
        temadventurer.carryFood(idF);
    }

    public void tryUseFood() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        String name = scanner.next();
        temadventurer.useFood(name);
    }

    public void recordLog() {
        int m = scanner.nextInt();
        int k = scanner.nextInt();
        ArrayList<String> temStrings = new ArrayList<>();
        String tem;
        for (int i = 0;i <= k;i++) {
            tem = scanner.nextLine();
            temStrings.add(tem);
        }
        String nameString = temStrings.get(0);
        ArrayList<String> nameArray = new ArrayList<>(Arrays.asList(nameString.trim().split(" +")));
        ArrayList<Adventurer> temAl = new ArrayList<>();
        for (String i : nameArray) {
            for (Adventurer j : adventurerHashMap.values()) {
                if (j.getName().equals(i)) {
                    j.setSavedHitPoint();
                    temAl.add(j);
                }
            }
        }
        fightLog.record(temStrings,adventurerHashMap);
        for (Adventurer i : temAl) {
            if (i.getHitpoint() <= (i.getSavedHitPoint() / 2)) {
                i.askForHelp();
            }
        }
    }

    public long getHelpFromWorkers(int id,int delta) {
        Adventurer temadventurer = adventurerHashMap.get(id);
        return temadventurer.provideHelp(delta);
    }

    public void checkByDate() {
        String date = scanner.next();
        fightLog.checkDate(date);
    }

    public void checkHit() {
        int id = scanner.nextInt();
        Adventurer temAdventurer = adventurerHashMap.get(id);
        String name = temAdventurer.getName();
        fightLog.checkHit(name);
    }

    public void checkHitBy() {
        int id = scanner.nextInt();
        Adventurer temAdventurer = adventurerHashMap.get(id);
        String name = temAdventurer.getName();
        fightLog.checkHitBy(name);
    }

    public void tryToPay() {
        int id = scanner.nextInt();
        Adventurer temAdventurer = adventurerHashMap.get(id);
        int idA = scanner.nextInt();
        temAdventurer.payForAdventurer(idA);
    }

    public void checkPrice() {
        int id = scanner.nextInt();
        Adventurer temAdventurer = adventurerHashMap.get(id);
        temAdventurer.getPriceSum(true);
    }

    public long getPriceOfAdventurer(int id) {
        Adventurer adventurerToCheck = adventurerHashMap.get(id);
        return adventurerToCheck.getPriceSum(false);
    }

    public void checkMost() {
        int id = scanner.nextInt();
        Adventurer temAdventurer = adventurerHashMap.get(id);
        temAdventurer.checkMostCom();
    }

    public void checkType() {
        int id = scanner.nextInt();
        Adventurer temAdventurer = adventurerHashMap.get(id);
        int idC = scanner.nextInt();
        temAdventurer.checkComType(idC);
    }

    public void adventurerSoldAll() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        temadventurer.soldAll();
    }

    public void adventurerBuy() {
        int id = scanner.nextInt();
        Adventurer temadventurer = adventurerHashMap.get(id);
        int idO = scanner.nextInt();
        String name = scanner.next();
        String ty = scanner.next();
        if (ty.equals("RegularBottle") || ty.equals("RegularEquipment") || ty.equals("Food")) {
            temadventurer.buy(idO,name,ty,0,0);
        }
        else if (ty.equals("CritEquipment")) {
            int critical = scanner.nextInt();
            temadventurer.buy(idO,name,ty,0,critical);
        }
        else {
            double ratio = scanner.nextDouble();
            temadventurer.buy(idO,name,ty,ratio,0);
        }
    }
}
