import java.util.HashMap;

public class Adventurer {
    private String nameAdv;
    private int idAdv;
    private HashMap<Integer, Bottle> bottleHashMap = new HashMap<>();
    private HashMap<Integer, Equipment> equipmentHashMap = new HashMap<>();

    public Adventurer(String name, int id) {
        this.nameAdv = name;
        this.idAdv = id;
    }

    public void AddBottle(int botID, String botName, int botCapacity) {
        Bottle bottle = new Bottle(botID, botName, botCapacity);
        this.bottleHashMap.put(botID, bottle);
    }

    public void DeleteBottle(int botId) {
        System.out.print(this.bottleHashMap.size() - 1);
        System.out.print(" ");
        System.out.println(this.bottleHashMap.get(botId).GetName());

        this.bottleHashMap.remove(botId);
    }

    public int GetBottleCount() {
        return this.bottleHashMap.size();
    }

    public boolean CheckBottle(int botId, String botName) {
        return this.bottleHashMap.containsKey(botId) && this.bottleHashMap.containsValue(botName);
    }

    public void AddEquipment(int equId, String equName, int equStar) {
        Equipment equipment = new Equipment(equId, equName, equStar);
        this.equipmentHashMap.put(equId, equipment);
    }

    public void DeleteEquipment(int equId) {
        System.out.print(this.equipmentHashMap.size() - 1);
        System.out.print(" ");
        System.out.println(this.equipmentHashMap.get(equId).GetName());

        this.equipmentHashMap.remove(equId);
    }

    public int GetEquipmentCount() {
        return this.equipmentHashMap.size();
    }

    public boolean CheckEquipment(int equId, String equName) {
        return this.equipmentHashMap.containsKey(equId)
                && this.equipmentHashMap.containsValue(equName);
    }

    public void AddStar(int equId) {
        Equipment equipment = equipmentHashMap.get(equId);
        equipment.AddStar();
        System.out.print(equipment.GetName());
        System.out.print(" ");
        System.out.println(equipment.GetStar());
    }
}
