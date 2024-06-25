import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FightLog {
    private ArrayList<ArrayList<String>> logContains;
    private ArrayList<ArrayList<String>> advInFight;
    private static String PATTERN1 = "(\\d{4}/\\d{2})-([^@#-]+)-(\\S+)";
    private static String PATTERN2 = "(\\d{4}/\\d{2})-([^@#-]+)@(\\S+)-(\\S+)";
    private static String PATTERN3 = "(\\d{4}/\\d{2})-([^@#-]+)@#-(\\S+)";
    private static Pattern pattern1 = Pattern.compile(PATTERN1);
    private static Pattern pattern2 = Pattern.compile(PATTERN2);
    private static Pattern pattern3 = Pattern.compile(PATTERN3);

    public FightLog() {
        logContains = new ArrayList<>();
        advInFight = new ArrayList<>();
    }

    public void record(ArrayList<String> logs, HashMap<Integer,Adventurer> adv) {
        String nameString = logs.get(0);
        ArrayList<String> nameArray = new ArrayList<>(Arrays.asList(nameString.trim().split(" +")));
        logs.remove(nameString);
        advInFight.add(nameArray);
        HashMap<String, Adventurer> temHM = new HashMap<>();
        for (String i : nameArray) {
            for (Adventurer j : adv.values()) {
                if (j.getName().equals(i)) { temHM.put(i, j); }
            }
        }
        this.mLog(logs,temHM,nameArray);
    }

    public void mLog(ArrayList<String> l,HashMap<String,Adventurer> t,ArrayList<String> arr) {
        ArrayList<String> logs = l;
        HashMap<String, Adventurer> temHM = t;
        ArrayList<String> nameArray = arr;
        ArrayList<String> temAR = new ArrayList<>();
        System.out.println("Enter Fight Mode");
        for (String i : logs) {
            Matcher m3 = pattern3.matcher(i);
            Matcher m2 = pattern2.matcher(i);
            Matcher m1 = pattern1.matcher(i);
            if (m3.find()) {
                Adventurer temAdv = temHM.get(m3.group(2));
                if (temAdv == null || temAdv.getEquipment(m3.group(3)) == null) {
                    System.out.println("Fight log error");
                } else {
                    Equipment temEquipment = temAdv.getEquipment(m3.group(3));
                    temAR.add(i);
                    for (String j : nameArray) {
                        if (!j.equals(m3.group(2))) {
                            temHM.get(j).getHurt(temAdv.getLevel(),temEquipment);
                            System.out.print(temHM.get(j).getHitpoint() + " ");
                        }
                    }
                    System.out.println();
                }
            } else if (m2.find()) {
                Adventurer adv1 = temHM.get(m2.group(2));
                Adventurer temAdv2 = temHM.get(m2.group(3));
                if (adv1 == null || temAdv2 == null || adv1.getEquipment(m2.group(4)) == null) {
                    System.out.println("Fight log error");
                } else {
                    Equipment temEquipment = adv1.getEquipment(m2.group(4));
                    temAR.add(i);
                    temAdv2.getHurt(adv1.getLevel(),temEquipment);
                    System.out.println(temAdv2.getId() + " " + temAdv2.getHitpoint());
                }
            } else if (m1.find()) {
                Adventurer temAdv = temHM.get(m1.group(2));
                if (temAdv == null) { System.out.println("Fight log error"); }
                else {
                    if (temAdv.whetherWithBottle(m1.group(3))) {
                        temAdv.useBottle(m1.group(3), false);
                        int id = temAdv.getIdTemBottle();
                        System.out.println(id + " " + temAdv.getHitpoint());
                        temAR.add(i);
                    }
                    else { System.out.println("Fight log error"); }
                }
            }
        }
        logContains.add(temAR);
    }

    public void checkDate(String date) {
        int ans = 0;
        for (ArrayList<String> i : logContains) {
            for (String j : i) {
                Matcher m3 = pattern3.matcher(j);
                Matcher m2 = pattern2.matcher(j);
                Matcher m1 = pattern1.matcher(j);
                if (m3.find() && m3.group(1).equals(date)) {
                    ans = 1;
                    String x = m3.group(2);
                    System.out.println(date + " " + x + " AOE-attacked with " + m3.group(3));
                } else if (m2.find() && m2.group(1).equals(date)) {
                    ans = 1;
                    String x = m2.group(2);
                    String y = m2.group(3);
                    String z = m2.group(4);
                    System.out.println(date + " " + x + " attacked " + y + " with " + z);
                } else if (m1.find() && m1.group(1).equals(date)) {
                    ans = 1;
                    System.out.println(date + " " + m1.group(2) + " used " + m1.group(3));
                }
            }
        }
        if (ans == 0) {
            System.out.println("No Matched Log");
        }
    }

    public void checkHit(String name) {
        int ans = 0;
        for (ArrayList<String> i : logContains) {
            for (String j : i) {
                Matcher m3 = pattern3.matcher(j);
                Matcher m2 = pattern2.matcher(j);
                if (m3.find() && m3.group(2).equals(name)) {
                    ans = 1;
                    String x = m3.group(1);
                    String y = m3.group(3);
                    System.out.println(x + " " + name + " AOE-attacked " + "with " + y);
                } else if (m2.find() && m2.group(2).equals(name)) {
                    ans = 1;
                    String x = m2.group(1);
                    String y = m2.group(3);
                    String z = m2.group(4);
                    System.out.println(x + " " + name + " attacked " + y + " with " + z);
                }
            }
        }
        if (ans == 0) {
            System.out.println("No Matched Log");
        }
    }

    public void checkHitBy(String name) {
        int ans = 0;
        int size = advInFight.size();
        for (int k = 0;k < size;k++) {
            ArrayList<String> i = advInFight.get(k);
            ArrayList<String> js = logContains.get(k);
            if (i.contains(name)) {
                for (String j : js) {
                    Matcher m3 = pattern3.matcher(j);
                    Matcher m2 = pattern2.matcher(j);
                    if (m3.find() && !m3.group(2).equals(name)) {
                        ans = 1;
                        String x = m3.group(1);
                        String y = m3.group(2);
                        System.out.println(x + " " + y + " AOE-attacked with " + m3.group(3));
                    } else if (m2.find() && m2.group(3).equals(name)) {
                        ans = 1;
                        String x = m2.group(1);
                        String y = m2.group(2);
                        String z = m2.group(3);
                        String xy = m2.group(4);
                        System.out.println(x + " " + y + " attacked " + z + " with " + xy);
                    }
                }
            }
        }
        if (ans == 0) {
            System.out.println("No Matched Log");
        }
    }
}
