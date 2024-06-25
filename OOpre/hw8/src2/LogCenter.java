import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogCenter {
    private static final HashMap<World, Integer> worldLogCount = new HashMap<>();

    // 世界日志数管理
    // 由于使用中明确只可能存在一个world，故不做判别
    public static void registerWorld(World world) {
        worldLogCount.put(world, 0);
    }

    public static int getWorldLogCount(World world) {
        return worldLogCount.get(world);
    }

    public static void setWorldLogCount(World world, int count) {
        worldLogCount.replace(world, count);
    }

    public static void endWorldLog(World world) {
        if (worldLogCount.get(world) > 0) {
            worldLogCount.replace(world, 0);
            world.exitFightMode();
        }
    }

    public static boolean inputLog(World world, String logString) {
        // 层次化解析第一层，用“-”分隔
        String[] logFragment = logString.trim().split("-");
        ArrayList<String> log = new ArrayList<>(Arrays.asList(logFragment));
        // 层次化解析第二层
        // 时间匹配正则表达式
        Pattern patTime = Pattern.compile("(\\d{4})/(\\d{2})");
        // 攻击匹配正则表达式
        Pattern patAttack1 = Pattern.compile("([^\\f\\n\\r\\t\\v@#-]+)@([^\\f\\n\\r\\t\\v@#-]+)");
        Pattern patAttack2 = Pattern.compile("([^\\f\\n\\r\\t\\v@#-]+)@#");
        // 解析时间
        int year;
        int month;
        Matcher matTime = patTime.matcher(log.get(0));
        if (matTime.find()) {
            year = Integer.parseInt(matTime.group(1));
            month = Integer.parseInt(matTime.group(2));
        } else {
            // 出现极端情况，直接return跳出，不会导致后面使用了未初始化的值
            return false;
        }
        // 解析中间的name
        Matcher matType2 = patAttack1.matcher(log.get(1));
        Matcher matType3 = patAttack2.matcher(log.get(1));
        if (matType2.find()) {
            // 一对一攻击
            return world.logType2(year, month, matType2.group(1), matType2.group(2), log.get(2));
        } else if (matType3.find()) {
            // 一对多攻击
            return world.logType3(year, month, matType3.group(1), log.get(2));
        } else {
            // 使用药水
            return world.logType1(year, month, log.get(1), log.get(2));
        }
    }
}
