import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FightLog {
    private ArrayList<String> fightLog = new ArrayList<>();

    public void enterFight(Scanner scanner,HashMap<Integer,Adventure> adventures,
                           ArrayList<String> string) {
        System.out.println("Enter Fight Mode");
        ArrayList<Adventure> fightAdventures = new ArrayList<>();
        int people = Integer.parseInt(string.get(1));
        int line = Integer.parseInt(string.get(2));
        int[] intArray = new int[15];
        for (int i = 0; i < people; ++i) {
            for (int key : adventures.keySet()) {
                if (adventures.get(key).getName().equals(string.get(3 + i))) {
                    fightAdventures.add(adventures.get(key));
                    fightAdventures.get(i).joinBattle();
                }
            }
        }
        int j = 0;
        for (Adventure adv : fightAdventures) {
            intArray[j++] = adv.getHitPoint();
        }
        scanLog(scanner,line,fightAdventures);
        j = 0;
        for (Adventure adv : fightAdventures) {
            if (adv.getHitPoint() <= (int)Math.floor((double) intArray[j] / 2)) {
                int lose = intArray[j] - adv.getHitPoint();
                adv.help(lose);
            }
            j++;
        }
        for (int i = 0; i < people; ++i) {
            for (int key : adventures.keySet()) {
                if (adventures.get(key).getName().equals(string.get(3 + i))) {
                    fightAdventures.add(adventures.get(key));
                    fightAdventures.get(i).dropBattle();
                }
            }
        }
    }

    public void scanLog(Scanner scanner,int line,ArrayList<Adventure> adventures) { //列表存放进入战斗状态的冒险者
        for (int i = 0; i < line; ++i) {
            String nextLine = scanner.nextLine(); // 读取本行指令
            readLine(nextLine,adventures);
        }
    }

    public void readLine(String nextLine,ArrayList<Adventure> adventures) {
        String regex1 = "(\\d{4})/(\\d{2})-([^@#]+)-([^@#]+)"; //YYYY/MM-{adv_name_1}-{name}
        String regex2 = "(\\d{4})/(\\d{2})-(\\S+)@([^@#]+)-(\\S+)";
        String regex3 = "(\\d{4})/(\\d{2})-(\\S+)@#-(\\S+)"; //YYYY/MM-{adv_name_1}@#-{name}
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        Pattern pattern3 = Pattern.compile(regex3);
        Matcher matcher1 = pattern1.matcher(nextLine);
        Matcher matcher2 = pattern2.matcher(nextLine);
        Matcher matcher3 = pattern3.matcher(nextLine);
        if (matcher1.matches()) {
            logMatcher1(matcher1,adventures,nextLine);
        }
        else if (matcher2.matches()) {
            logMatcher2(matcher2,adventures,nextLine);
        }
        else if (matcher3.matches()) {
            logMatcher3(matcher3,adventures,nextLine);
        }
        else {
            System.out.println("fail to match");
        }
    }

    public void logMatcher1(Matcher matcher,ArrayList<Adventure> adventures,String nextLine) {
        String year = matcher.group(1);
        String month = matcher.group(2);
        String advName = matcher.group(3);
        String name = matcher.group(4);
        int flag = 0;
        for (Adventure adv : adventures) {
            if (adv.getName().equals(advName)) {
                flag = 1;
                if (adv.useBottle(name)) {
                    fightLog.add(nextLine);
                }
                else {
                    System.out.println("Fight log error");
                }
            }
        }
        if (flag == 0) {
            System.out.println("Fight log error");
        }
    }

    public void logMatcher2(Matcher matcher,ArrayList<Adventure> adventures,String nextLine) {
        String year = matcher.group(1);//YYYY/MM-{adv_name_1}@{adv_name_2}-{name}
        String month = matcher.group(2);
        String advName1 = matcher.group(3);
        String advName2 = matcher.group(4);
        String name = matcher.group(5);
        int num1 = findAdv(adventures,advName1);//数组下标
        int num2 = findAdv(adventures,advName2);
        if (num1 > -1 && num2 > -1) {
            int index = -1;
            for (int key : adventures.get(num1).getEquipments().keySet()) {
                if (adventures.get(num1).getEquipments().get(key).getName().equals(name) &&
                        adventures.get(num1).getEquipments().get(key).isInBackpack()) {
                    index = key;
                }
            }
            if (index == -1) {
                System.out.println("Fight log error");
            } else {
                adventures.get(num2).HitPointDecrease(
                        adventures.get(num1).getEquipments().get(index),
                        adventures.get(num1));
                fightLog.add(nextLine);
                adventures.get(num1).getAdvLog().addAttackerLog(nextLine);
                adventures.get(num2).getAdvLog().addAttackedLog(nextLine);
                System.out.println(adventures.get(num2).getId() + " " +
                        adventures.get(num2).getHitPoint());
            }
        }
        else {
            System.out.println("Fight log error");
        }
    }

    public void logMatcher3(Matcher matcher,ArrayList<Adventure> adventures,String nextLine) {
        String year = matcher.group(1);
        String month = matcher.group(2);
        String advName = matcher.group(3);
        String name = matcher.group(4);
        int num = findAdv(adventures,advName); //发起攻击者下标
        if (num > -1) {
            int index = -1;
            for (int key :adventures.get(num).getEquipments().keySet()) {
                if (adventures.get(num).getEquipments().get(key).getName().equals(name) &&
                        adventures.get(num).getEquipments().get(key).isInBackpack()) {
                    index = key;
                }
            }
            if (index == -1) {
                System.out.println("Fight log error");
            }
            else {
                for (Adventure adv : adventures) {
                    if (!adv.getName().equals(advName)) { //不是发起攻击者
                        adv.HitPointDecrease(adventures.get(num).getEquipments().get(index),
                                adventures.get(num));
                        System.out.print(adv.getHitPoint() + " ");
                        adv.getAdvLog().addAttackedLog(nextLine);
                    }
                }
                System.out.println();
                fightLog.add(nextLine);
                adventures.get(num).getAdvLog().addAttackerLog(nextLine);
            }
        }
        else {
            System.out.println("Fight log error");
        }
    }

    public Integer findAdv(ArrayList<Adventure> adventures,String name) { //用名字寻找一个容器中的冒险者
        for (int i = 0; i < adventures.size(); i++) {
            if (adventures.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1; //需保证冒险者在容器中,返回下标
    }

    public void getFightLog(String string) {
        int flag = 0;
        for (String log : fightLog) {
            String regex = "(\\d{4}/\\d{2})-(\\S+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(log);
            //System.out.println(log);
            if (matcher.matches()) {
                String date = matcher.group(1);
                if (date.equals(string)) {
                    flag = 1;
                    String regex1 = "(\\d{4})/(\\d{2})-([^@#]+)-([^@#]+)";
                    String regex2 = "(\\d{4})/(\\d{2})-(\\S+)@([^@#]+)-(\\S+)";
                    String regex3 = "(\\d{4})/(\\d{2})-(\\S+)@#-(\\S+)";
                    Pattern pattern1 = Pattern.compile(regex1);
                    Pattern pattern2 = Pattern.compile(regex2);
                    Pattern pattern3 = Pattern.compile(regex3);
                    Matcher matcher1 = pattern1.matcher(log);
                    Matcher matcher2 = pattern2.matcher(log);
                    Matcher matcher3 = pattern3.matcher(log);
                    if (matcher1.matches()) { //YYYY/MM {adv_name_1} used {name}
                        String year = matcher1.group(1);
                        String month = matcher1.group(2);
                        String advName = matcher1.group(3);
                        String name = matcher1.group(4);
                        System.out.println(year + "/" + month + " " + advName + " used " + name);
                    } else if (matcher2.matches()) {
                        String year = matcher2.group(1);
                        String month = matcher2.group(2);
                        String advName1 = matcher2.group(3);
                        String advName2 = matcher2.group(4);
                        String name = matcher2.group(5);
                        System.out.println(year + "/" + month + " " + advName1 + " attacked " +
                                advName2 + " with " + name);
                    } else if (matcher3.matches()) { //YYYY/MM {adv_name_1} AOE-attacked with {name}
                        String year = matcher3.group(1);
                        String month = matcher3.group(2);
                        String advName = matcher3.group(3);
                        String name = matcher3.group(4);
                        System.out.println(year + "/" + month + " " + advName +
                                " AOE-attacked with " + name);
                    } else {
                        System.out.println("fail to match");
                    }
                }
            }
            else {
                System.out.println("fail to match");
            }
        }
        if (fightLog.isEmpty() || flag == 0) {
            System.out.println("No Matched Log");
        }
    }

    public void addFightLog(String string) {
        fightLog.add(string);
    }
}
