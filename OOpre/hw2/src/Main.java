import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, Adventurer> adventurerHashMap = new HashMap<>();

        int n = scanner.nextInt();
        int type;

        for (int i = 0; i < n; i++) {
            type = scanner.nextInt();
            if (type == 1) {
                int id = scanner.nextInt();
                String name = scanner.next();

                Adventurer adventurer = new Adventurer(name, id);
                adventurerHashMap.put(id, adventurer);
            } else if (type == 2) {
                int advId = scanner.nextInt();
                int botId = scanner.nextInt();
                String name = scanner.next();
                int capacity = scanner.nextInt();

                Adventurer adventurer = adventurerHashMap.get(advId);
                adventurer.AddBottle(botId, name, capacity);
            } else if (type == 3) {
                int advId = scanner.nextInt();
                int botId = scanner.nextInt();

                Adventurer adventurer = adventurerHashMap.get(advId);
                adventurer.DeleteBottle(botId);
            } else if (type == 4) {
                int advId = scanner.nextInt();
                int equId = scanner.nextInt();
                String name = scanner.next();
                int equStar = scanner.nextInt();

                Adventurer adventurer = adventurerHashMap.get(advId);
                adventurer.AddEquipment(equId, name, equStar);
            } else if (type == 5) {
                int advId = scanner.nextInt();
                int equId = scanner.nextInt();

                Adventurer adventurer = adventurerHashMap.get(advId);
                adventurer.DeleteEquipment(equId);
            } else if (type == 6) {
                int advId = scanner.nextInt();
                int equId = scanner.nextInt();

                Adventurer adventurer = adventurerHashMap.get(advId);
                adventurer.AddStar(equId);
            }
        }
    }
}
