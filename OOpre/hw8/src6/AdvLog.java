import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvLog {
    private ArrayList<String> attacker;
    private ArrayList<String> attacked;

    public AdvLog() {
        attacker = new ArrayList<String>();
        attacked = new ArrayList<String>();
    }

    public void addAttackerLog(String string) {
        attacker.add(string);
    }

    public void addAttackedLog(String string) {
        attacked.add(string);
    }

    public void getAttackerLog() {
        String regex1 = "(\\d{4})/(\\d{2})-(\\S+)@([^@#]+)-(\\S+)";
        String regex2 = "(\\d{4})/(\\d{2})-(\\S+)@#-(\\S+)"; //YYYY/MM-{adv_name_1}@#-{name}
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        if (attacker.isEmpty()) {
            System.out.println("No Matched Log");
        }
        else {
            for (String string : attacker) {
                Matcher matcher1 = pattern1.matcher(string);
                Matcher matcher2 = pattern2.matcher(string);
                if (matcher1.matches()) { //YYYY/MM {adv_name_1} attacked {adv_name_2} with {name}
                    String year = matcher1.group(1);
                    String month = matcher1.group(2);
                    String advName1 = matcher1.group(3);
                    String advName2 = matcher1.group(4);
                    String name = matcher1.group(5);
                    System.out.println(year + "/" + month + " " + advName1 + " attacked " +
                            advName2 + " with " + name);
                } else if (matcher2.matches()) { //YYYY/MM {adv_name_1} AOE-attacked with {name}
                    String year = matcher2.group(1);
                    String month = matcher2.group(2);
                    String advName = matcher2.group(3);
                    String name = matcher2.group(4);
                    System.out.println(year + "/" + month + " " + advName +
                            " AOE-attacked with " + name);
                } else {
                    System.out.println("fail to match");
                }
            }
        }
    }

    public void getAttackedLog() {
        String regex1 = "(\\d{4})/(\\d{2})-(\\S+)@([^@#]+)-(\\S+)";
        String regex2 = "(\\d{4})/(\\d{2})-(\\S+)@#-(\\S+)"; //YYYY/MM-{adv_name_1}@#-{name}
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        if (attacked.isEmpty()) {
            System.out.println("No Matched Log");
        }
        for (String string : attacked) {
            Matcher matcher1 = pattern1.matcher(string);
            Matcher matcher2 = pattern2.matcher(string);
            if (matcher1.matches()) { //YYYY/MM {adv_name_1} attacked {adv_name_2} with {name}
                String year = matcher1.group(1);
                String month = matcher1.group(2);
                String advName1 = matcher1.group(3);
                String advName2 = matcher1.group(4);
                String name = matcher1.group(5);
                System.out.println(year + "/" + month + " " + advName1 + " attacked " +
                        advName2 + " with " + name);
            } else if (matcher2.matches()) { //YYYY/MM {adv_name_1} AOE-attacked with {name}
                String year = matcher2.group(1);
                String month = matcher2.group(2);
                String advName = matcher2.group(3);
                String name = matcher2.group(4);
                System.out.println(year + "/" + month + " " + advName +
                        " AOE-attacked with " + name);
            }
            else {
                System.out.println("fail to match");
            }
        }
    }
}
