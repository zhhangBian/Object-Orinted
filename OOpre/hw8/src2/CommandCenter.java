import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandCenter {
    private static boolean command1(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.addAdv(advId, command.get(2));
    }

    private static boolean command2(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int botId = Integer.parseInt(command.get(2));
        int capacity = Integer.parseInt(command.get(4));
        long price = Long.parseLong(command.get(5));
        double ratio = command.size() >= 8 ? Double.parseDouble(command.get(7)) : 0.0;
        return world.addBotToAdv(advId, botId, command.get(3), capacity,
                price, command.get(6), ratio);
    }

    private static boolean command3(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int botId = Integer.parseInt(command.get(2));
        return world.subBotOfAdv(advId, botId);
    }

    private static boolean command4(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int equId = Integer.parseInt(command.get(2));
        int star = Integer.parseInt(command.get(4));
        long price = Long.parseLong(command.get(5));
        int critical = command.get(6).equals("CritEquipment") ?
                Integer.parseInt(command.get(7)) : 0;
        double ratio = command.get(6).equals("EpicEquipment") ?
                Double.parseDouble(command.get(7)) : 0.0;
        return world.addEquToAdv(advId, equId, command.get(3), star,
                price, command.get(6), critical, ratio);
    }

    private static boolean command5(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int equId = Integer.parseInt(command.get(2));
        return world.subEquOfAdv(advId, equId);
    }

    private static boolean command6(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int equId = Integer.parseInt(command.get(2));
        return world.addStarOfEquOfAdv(advId, equId);
    }

    private static boolean command7(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int foodId = Integer.parseInt(command.get(2));
        int energy = Integer.parseInt(command.get(4));
        long price = Long.parseLong(command.get(5));
        return world.addFoodToAdv(advId, foodId, command.get(3), energy,
                price);
    }

    private static boolean command8(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int foodId = Integer.parseInt(command.get(2));
        return world.subFoodOfAdv(advId, foodId);
    }

    private static boolean command9(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int equId = Integer.parseInt(command.get(2));
        return world.advCarryEqu(advId, equId);
    }

    private static boolean command10(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int botId = Integer.parseInt(command.get(2));
        return world.advCarryBot(advId, botId);
    }

    private static boolean command11(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int foodId = Integer.parseInt(command.get(2));
        return world.advCarryFood(advId, foodId);
    }

    private static boolean command12(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.advUseBot(advId, command.get(2));
    }

    private static boolean command13(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.advUseFood(advId, command.get(2));
    }

    private static boolean command14(World world, ArrayList<String> command) {
        int m = Integer.parseInt(command.get(1));
        int k = Integer.parseInt(command.get(2));
        // 在logCenter中
        LogCenter.setWorldLogCount(world, k);
        return world.addFightAdv(m, command);
    }

    private static boolean command15(World world, ArrayList<String> command) {
        // 时间匹配正则表达式
        Pattern patTime = Pattern.compile("(\\d{4})/(\\d{2})");
        // 解析时间
        Matcher matTime = patTime.matcher(command.get(1));
        if (matTime.find()) {
            int year = Integer.parseInt(matTime.group(1));
            int month = Integer.parseInt(matTime.group(2));
            return world.queryLogByTime(year, month);
        } else {
            // 出现极端情况，直接return跳出，不会导致后面使用了未初始化的值
            return false;
        }
    }

    private static boolean command16(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.queryLogByAttacker(advId);
    }

    private static boolean command17(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.queryLogByWounded(advId);
    }

    private static boolean command18(World world, ArrayList<String> command) {
        int advId1 = Integer.parseInt(command.get(1));
        int advId2 = Integer.parseInt(command.get(2));
        return world.advHireAdv(advId1, advId2);
    }

    private static boolean command19(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.queryAdvPrice(advId);
    }

    private static boolean command20(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.queryAdvPriceMax(advId);
    }

    private static boolean command21(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int comId = Integer.parseInt(command.get(2));
        return world.queryAdvComPrice(advId, comId);
    }

    private static boolean command22(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        return world.advSellAll(advId);
    }

    private static boolean command23(World world, ArrayList<String> command) {
        int advId = Integer.parseInt(command.get(1));
        int id = Integer.parseInt(command.get(2));
        int critical = command.get(4).equals("CritEquipment") ?
                Integer.parseInt(command.get(5)) : 0;
        double ratio = (command.get(4).equals("RecoverBottle") ||
                command.get(4).equals("ReinforcedBottle") ||
                command.get(4).equals("EpicEquipment")) ?
                Double.parseDouble(command.get(5)) : 0.0;
        return world.advBuyFromStore(advId, id, command.get(3), command.get(4), critical, ratio);
    }

    public static boolean inputCommand(World world, String commandString) {
        // 使用**若干**个空格分割
        String[] commandFragment = commandString.trim().split(" +");
        ArrayList<String> command = new ArrayList<>(Arrays.asList(commandFragment));
        switch (Integer.parseInt(command.get(0))) {
            case 1:
                return command1(world, command);
            case 2:
                return command2(world, command);
            case 3:
                return command3(world, command);
            case 4:
                return command4(world, command);
            case 5:
                return command5(world, command);
            case 6:
                return command6(world, command);
            case 7:
                return command7(world, command);
            case 8:
                return command8(world, command);
            case 9:
                return command9(world, command);
            case 10:
                return command10(world, command);
            case 11:
                return command11(world, command);
            case 12:
                return command12(world, command);
            case 13:
                return command13(world, command);
            case 14:
                return command14(world, command);
            case 15:
                return command15(world, command);
            case 16:
                return command16(world, command);
            case 17:
                return command17(world, command);
            case 18:
                return command18(world, command);
            case 19:
                return command19(world, command);
            case 20:
                return command20(world, command);
            case 21:
                return command21(world, command);
            case 22:
                return command22(world, command);
            case 23:
                return command23(world, command);
            default:
                return false;
        }
    }
}
