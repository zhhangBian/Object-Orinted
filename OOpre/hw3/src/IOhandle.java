import java.util.HashMap;
import java.util.Scanner;

public class IOhandle {

    //type=1
    public void AddAdventurer(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int id = scanner.nextInt();
        String name = scanner.next();
        Adventurer adventurer = new Adventurer(name, id);
        adventurerHashMap.put(id, adventurer);
    }

    //type=2
    public void AddBottle(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int botId = scanner.nextInt();
        String name = scanner.next();
        int capacity = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.AddBottle(botId, name, capacity);
    }

    //type=3
    public void DeleteBottle(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int botId = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.DeleteBottle(botId);
    }

    //type=4
    public void AddEquipment(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();
        String name = scanner.next();
        int equStar = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.AddEquipment(equId, name, equStar);
    }

    //type=5
    public void DeleteEquipment(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.DeleteEquipment(equId);
    }

    //type=6
    public void AddStar(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.AddStar(equId);
    }

    //type=7
    public void AddFood(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int foodId = scanner.nextInt();
        String foodName = scanner.next();
        int energy = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.AddFood(foodId, foodName, energy);
    }

    //type=8
    public void DeleteFood(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int foodId = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.DeleteFood(foodId);
    }

    //type=9
    public void TakeEquipment(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.TakeEquipment(equId);
    }

    //type=10
    public void TakeBottle(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int botID = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.TakeBottle(botID);
    }

    //type=11
    public void TakeFood(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        int foodId = scanner.nextInt();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.TakeFood(foodId);
    }

    //type=12
    public void UseBottle(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        String botName = scanner.next();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.UseBottle(botName);
    }

    //type=13
    public void UseFood(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        String foodName = scanner.next();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.UseFood(foodName);
    }
}