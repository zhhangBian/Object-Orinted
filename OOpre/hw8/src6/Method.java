import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Method {
    public void ScanAndProcess() {
        HashMap<Integer,Adventure> adventures = new HashMap<>();//放置所有adventure的容器
        FightLog fightLog = new FightLog();//放置所有有效战斗日志
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim()); // 读取行数
        for (int i = 0; i < n; ++i) {
            String nextLine = scanner.nextLine(); // 读取本行指令
            String[] strings = nextLine.trim().split(" +"); // 按空格对行进行分割
            ArrayList<String> s = new ArrayList<>(Arrays.asList(strings)); // 将指令分割后的各个部分存进容器中
            int sign = Integer.parseInt(s.get(0));
            if (sign == 1) {
                int id = Integer.parseInt(s.get(1));
                adventures.put(id, new Adventure(id, s.get(2)));
            } else if (sign == 2) {
                adventures.get(Integer.parseInt(s.get(1))).addBottle(s);
            } else if (sign == 3) {
                adventures.get(Integer.parseInt(s.get(1))).deleteBottle(s.get(2));
            } else if (sign == 4) {
                adventures.get(Integer.parseInt(s.get(1))).addEquipment(s);
            } else if (sign == 5) {
                adventures.get(Integer.parseInt(s.get(1))).deleteEquipment(s.get(2));
            } else if (sign == 6) {
                adventures.get(Integer.parseInt(s.get(1))).raiseEquipment(s.get(2));
            } else if (sign == 7) {
                adventures.get(Integer.parseInt(s.get(1))).addFood(s);
            } else if (sign == 8) {
                adventures.get(Integer.parseInt(s.get(1))).deleteFood(s.get(2));
            } else if (sign == 9) {
                adventures.get(Integer.parseInt(s.get(1))).takeEquipment(s.get(2));
            } else if (sign == 10) {
                adventures.get(Integer.parseInt(s.get(1))).takeBottle(s.get(2));
            } else if (sign == 11) {
                adventures.get(Integer.parseInt(s.get(1))).takeFood(s.get(2));
            } else if (sign == 12) {
                adventures.get(Integer.parseInt(s.get(1))).useBottle(s.get(2));
            } else if (sign == 13) {
                adventures.get(Integer.parseInt(s.get(1))).useFood(s.get(2));
            } else if (sign == 14) { fightLog.enterFight(scanner,adventures, s); }
            else if (sign == 15) {
                fightLog.getFightLog(s.get(1));
            } else if (sign == 16) {
                adventures.get(Integer.parseInt(s.get(1))).getAdvLog().getAttackerLog();
            } else if (sign == 17) {
                adventures.get(Integer.parseInt(s.get(1))).getAdvLog().getAttackedLog();
            } else if (sign == 18) {
                Adventure adventure = adventures.get(Integer.parseInt(s.get(2)));
                adventures.get(Integer.parseInt(s.get(1))).hireAdventure(adventure);
            } else if (sign == 19) {
                adventures.get(Integer.parseInt(s.get(1))).getAdvPrice();
            } else if (sign == 20) {
                adventures.get(Integer.parseInt(s.get(1))).getLargestPrice();
            } else if (sign == 21) {
                adventures.get(Integer.parseInt(s.get(1))).getTypeName(Integer.parseInt(s.get(2)));
            } else if (sign == 22) {
                adventures.get(Integer.parseInt(s.get(1))).sellAll();
            } else {
                adventures.get(Integer.parseInt(s.get(1))).buyFromShop(s);
            }
        }
    }
}
