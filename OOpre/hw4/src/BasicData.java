import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicData {
    private HashMap<Integer, Adventurer> adventurerHashMap;
    private ArrayList<Adventurer> inFightAdventurer;
    private ArrayList<Record> records;

    public BasicData() {
        this.adventurerHashMap = new HashMap<>();
        this.inFightAdventurer = new ArrayList<>();
        this.records = new ArrayList<>();
    }

    //adventurer

    public HashMap<Integer, Adventurer> GetHashMap() {
        return this.adventurerHashMap;
    }

    public int GetAdventurerId(String advName) {
        for (Map.Entry<Integer, Adventurer> entry : adventurerHashMap.entrySet()) {
            if (advName.equals(entry.getValue().GetName())) {
                return entry.getValue().GetId();
            }
        }
        return 0;
    }

    public Adventurer GetAdventurer(String advName) {
        return this.adventurerHashMap.get(GetAdventurerId(advName));
    }

    //record list
    public void AddRecord(String date, int type, int advId, String advName,
                          int useID, String useName, BasicData data) {
        Record record = new Record(date, type, advId, advName, useID, useName, data);
        this.records.add(record);
    }

    public void AddRecord(String date, int type, int advId, String advName,
                          int fightedId, int useID, String useName, BasicData data) {
        Record record = new Record(date, type, advId, advName, fightedId, useID, useName, data);
        this.records.add(record);
    }

    public void CheckRecordDate(String date) {
        boolean checkNoRecord = true;
        for (Record record : records) {
            if (record.GetDate().equals(date)) {
                checkNoRecord = false;
                switch (record.GetType()) {
                    case 1:
                        System.out.println(date + " " + record.GetFighterName()
                                + " used " + record.GetUseName());
                        break;
                    case 2:
                        System.out.println(date + " " + record.GetFighterName() + " attacked "
                                + this.adventurerHashMap.get(record.GetFightedId()).GetName()
                                + " with " + record.GetUseName());
                        break;
                    case 3:
                        System.out.println(date + " " + record.GetFighterName()
                                + " AOE-attacked with " + record.GetUseName());
                        break;
                    default:
                        break;
                }
            }
        }

        if (checkNoRecord) {
            System.out.println("No Matched Log");
        }
    }

    public void PrintFight(Record record) {
        switch (record.GetType()) {
            case 2:
                System.out.println(record.GetDate() + " " + record.GetFighterName()
                        + " attacked "
                        + this.adventurerHashMap.get(record.GetFightedId()).GetName()
                        + " with " + record.GetUseName());
                break;
            case 3:
                System.out.println(record.GetDate() + " " + record.GetFighterName()
                        + " AOE-attacked with " + record.GetUseName());
                break;
            default:
                break;
        }
    }

    public void CheckRecordFighter(int advId) {
        boolean checkNoRecord = true;
        for (Record record : records) {
            if (record.GetFighterId() == advId
                    && (record.GetType() == 2 || record.GetType() == 3)) {
                checkNoRecord = false;
                PrintFight(record);
            }
        }

        if (checkNoRecord) {
            System.out.println("No Matched Log");
        }
    }

    public void CheckRecordFighted(int advId) {
        boolean checkNoRecord = true;
        for (Record record : records) {
            if (record.IsFighted(advId)
                    && (record.GetType() == 2 || record.GetType() == 3)) {
                checkNoRecord = false;
                PrintFight(record);
            }
        }

        if (checkNoRecord) {
            System.out.println("No Matched Log");
        }

    }

    //in fight adventurer list
    public void AddAdventurerInFight(Adventurer adventurer) {
        this.inFightAdventurer.add(adventurer);
    }

    public void OutFightAll(HashMap<Integer, Adventurer> adventurerHashMap) {
        this.inFightAdventurer.clear();
        for (Map.Entry<Integer, Adventurer> entry : adventurerHashMap.entrySet()) {
            entry.getValue().OutFight();
        }
    }

    public ArrayList<Adventurer> GetAdventurersList() {
        return this.inFightAdventurer;
    }
}
