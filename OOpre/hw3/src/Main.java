import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //FileInputStream in=new FileInputStream("input.txt");
        //System.setIn(in);
        //PrintStream out=new PrintStream("out.txt");
        //System.setOut(out);

        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, Adventurer> adventurerHashMap = new HashMap<>();
        IOhandle io = new IOhandle();

        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            int type = scanner.nextInt();
            switch (type) {
                case 1:
                    io.AddAdventurer(scanner, adventurerHashMap);
                    break;
                case 2:
                    io.AddBottle(scanner, adventurerHashMap);
                    break;
                case 3:
                    io.DeleteBottle(scanner, adventurerHashMap);
                    break;
                case 4:
                    io.AddEquipment(scanner, adventurerHashMap);
                    break;
                case 5:
                    io.DeleteEquipment(scanner, adventurerHashMap);
                    break;
                case 6:
                    io.AddStar(scanner, adventurerHashMap);
                    break;
                case 7:
                    io.AddFood(scanner, adventurerHashMap);
                    break;
                case 8:
                    io.DeleteFood(scanner, adventurerHashMap);
                    break;
                case 9:
                    io.TakeEquipment(scanner, adventurerHashMap);
                    break;
                case 10:
                    io.TakeBottle(scanner, adventurerHashMap);
                    break;
                case 11:
                    io.TakeFood(scanner, adventurerHashMap);
                    break;
                case 12:
                    io.UseBottle(scanner, adventurerHashMap);
                    break;
                case 13:
                    io.UseFood(scanner, adventurerHashMap);
                    break;
                default:
                    break;
            }
        }
    }
}
