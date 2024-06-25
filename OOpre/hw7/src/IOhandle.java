import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOhandle {
    public void HandleType(Scanner scanner, BasicData basicData)
            throws NoSuchMethodException, SecurityException {
        String methodName = String.format("Op%d", scanner.nextInt());
        Method method = this.getClass().getMethod(methodName, Scanner.class, BasicData.class);
        try {
            method.invoke(this, scanner, basicData);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //type=1
    public void Op1(Scanner scanner, BasicData basicData) {
        int id = scanner.nextInt();
        String name = scanner.next();
        basicData.AddAdventurer(new Adventurer(name, id));
    }

    //type=2
    public void Op2(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int botId = scanner.nextInt();
        String name = scanner.next();
        int capacity = scanner.nextInt();
        long price = scanner.nextLong();
        String type = scanner.next();

        Adventurer adventurer = basicData.GetAdventurer(advId);

        if (type.equals("RegularBottle")) {
            adventurer.AddBottle(new RegularBottle(botId, name, capacity, price));
        } else if (type.equals("ReinforcedBottle")) {
            double ratio = scanner.nextDouble();
            adventurer.AddBottle(new ReinforcedBottle(botId, name, capacity, price, ratio));
        } else if (type.equals("RecoverBottle")) {
            double ratio = scanner.nextDouble();
            adventurer.AddBottle(new RecoverBottle(botId, name, capacity, price, ratio));
        }
    }

    //type=3
    public void Op3(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int botId = scanner.nextInt();

        basicData.GetAdventurer(advId).SellBottle(botId, basicData.GetShop());
    }

    //type=4
    public void Op4(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();
        String equName = scanner.next();
        int equStar = scanner.nextInt();
        long price = scanner.nextLong();
        String type = scanner.next();

        Adventurer adventurer = basicData.GetAdventurer(advId);

        if (type.equals("RegularEquipment")) {
            adventurer.AddEquipment(new RegularEquipment(equId, equName, equStar, price));
        } else if (type.equals("CritEquipment")) {
            int critical = scanner.nextInt();
            adventurer.AddEquipment(new CritEquipment(equId, equName, equStar, price, critical));
        } else if (type.equals("EpicEquipment")) {
            double ratio = scanner.nextDouble();
            adventurer.AddEquipment(new EpicEquipment(equId, equName, equStar, price, ratio));
        }
    }

    //type=5
    public void Op5(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();

        basicData.GetAdventurer(advId).SellEquipment(equId, basicData.GetShop());
    }

    //type=6
    public void Op6(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();

        basicData.GetAdventurer(advId).AddStar(equId);
    }

    //type=7
    public void Op7(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int foodId = scanner.nextInt();
        String foodName = scanner.next();
        int energy = scanner.nextInt();
        long price = scanner.nextLong();

        basicData.GetAdventurer(advId).AddFood(new Food(foodId, foodName, energy, price));
    }

    //type=8
    public void Op8(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int foodId = scanner.nextInt();

        basicData.GetAdventurer(advId).SellFood(foodId, basicData.GetShop());
    }

    //type=9
    public void Op9(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int equId = scanner.nextInt();

        basicData.GetAdventurer(advId).TakeEquipment(equId);
    }

    //type=10
    public void Op10(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int botID = scanner.nextInt();

        basicData.GetAdventurer(advId).TakeBottle(botID);
    }

    //type=11
    public void Op11(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int foodId = scanner.nextInt();

        basicData.GetAdventurer(advId).TakeFood(foodId);
    }

    //type=12
    public void Op12(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        String botName = scanner.next();

        basicData.GetAdventurer(advId).UseBottle(botName, false);
    }

    //type=13
    public void Op13(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        String foodName = scanner.next();

        basicData.GetAdventurer(advId).UseFood(foodName);
    }

    //type=14
    public void Op14(Scanner scanner, BasicData basicData) {
        int m = scanner.nextInt();
        int k = scanner.nextInt();

        System.out.println("Enter Fight Mode");

        String stringBottle =
                "((\\d{4})/((1[0-2])|(0?[1-9])))-([^(@|#|\\-)]+)-([^(@|#|\\-)]+)";
        String stringFightOne =
                "((\\d{4})/((1[0-2])|(0?[1-9])))-([^(@|#|\\-)]+)@([^(@|#|\\-)]+)-([^(@|#|\\-)]+)";
        String stringFightMore =
                "((\\d{4})/((1[0-2])|(0?[1-9])))-([^(@|#|\\-)]+)@#-([^(@|#|\\-)]+)";

        Pattern patternBottle = Pattern.compile(stringBottle);
        Pattern patternFightOne = Pattern.compile(stringFightOne);
        Pattern patternFightMore = Pattern.compile(stringFightMore);

        for (int i = 0; i < m; i++) {
            String advName = scanner.next();
            Adventurer adventurer = basicData.GetAdventurer(advName);
            basicData.AddAdventurerInFight(adventurer);
        }

        for (int i = 0; i < k; i++) {
            String issue = scanner.next();

            Matcher matcherBottle = patternBottle.matcher(issue);
            Matcher matcherFightOne = patternFightOne.matcher(issue);
            Matcher matcherFightMore = patternFightMore.matcher(issue);

            if (matcherBottle.find()) {
                this.FightUseBottle(basicData, matcherBottle);
            } else if (matcherFightOne.find()) {
                this.FightOne(basicData, matcherFightOne);
            } else if (matcherFightMore.find()) {
                this.FightMore(basicData, matcherFightMore);
            }
        }

        basicData.OutFightAll();
    }

    public void FightUseBottle(BasicData basicData, Matcher matcher) {
        String date = matcher.group(1);
        String advName = matcher.group(6);
        String botName = matcher.group(7);
        Adventurer adventurer = basicData.GetAdventurer(advName);

        if (adventurer.GetStatus() && adventurer.CheckTakeBottle(botName)) {
            basicData.AddRecord(date, 1, adventurer.GetId(), advName,
                    adventurer.GetBottleId(botName), botName);
            adventurer.UseBottle(botName, true);
        } else {
            System.out.println("Fight log error");
        }
    }

    public void FightOne(BasicData basicData, Matcher matcher) {
        String date = matcher.group(1);
        String fighterName = matcher.group(6);
        String fightedName = matcher.group(7);
        String equName = matcher.group(8);

        Adventurer fighter = basicData.GetAdventurer(fighterName);
        Adventurer fighted = basicData.GetAdventurer(fightedName);

        if (fighter.GetStatus() && fighted.GetStatus()
                && fighter.CheckTakeEquipment(equName)) {
            Equipment equipment = fighter.GetEquipment(equName);
            basicData.AddRecord(date, 2, fighter.GetId(), fighterName, fighted.GetId(),
                    equipment.GetId(), equName);
            fighted.GetAttacked(fighter.GetLevel(), equipment);

            System.out.println(fighted.GetId() + " " + fighted.GetHitPoint());
        } else {
            System.out.println("Fight log error");
        }
    }

    public void FightMore(BasicData basicData, Matcher matcher) {
        String date = matcher.group(1);
        String advName = matcher.group(6);
        String equName = matcher.group(7);

        Adventurer fighter = basicData.GetAdventurer(advName);
        if (fighter.GetStatus() && fighter.CheckTakeEquipment(equName)) {
            Equipment equipment = fighter.GetEquipment(equName);
            ArrayList<Adventurer> advList = basicData.GetAdvInFightList();
            basicData.AddRecord(date, 3, fighter.GetId(), advName
                    , equipment.GetId(), equName);

            for (Adventurer fighted : advList) {
                if (!fighted.equals(fighter)) {
                    fighted.GetAttacked(fighter.GetLevel(), equipment);
                    System.out.print(fighted.GetHitPoint() + " ");
                }
            }
            System.out.println();
        } else {
            System.out.println("Fight log error");
        }
    }

    //type=15
    public void Op15(Scanner scanner, BasicData basicData) {
        String date = scanner.next();
        basicData.CheckRecordDate(date);
    }

    //type=16
    public void Op16(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        basicData.CheckRecordFighter(advId);
    }

    //type=17
    public void Op17(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        basicData.CheckRecordFighted(advId);
    }

    //type=18
    public void Op18(Scanner scanner, BasicData basicData) {
        int employerId = scanner.nextInt();
        int employeeId = scanner.nextInt();
        Adventurer employer = basicData.GetAdventurer(employerId);
        Adventurer employee = basicData.GetAdventurer(employeeId);
        employer.HireAdventurer(employee);
    }

    //type=19
    public void Op19(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        Adventurer adventurer = basicData.GetAdventurer(advId);
        System.out.print(adventurer.GetCount());
        System.out.print(" ");
        System.out.println(adventurer.GetCommodity() - adventurer.GetMoney());
    }

    //type=20
    public void Op20(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        System.out.println(basicData.GetAdventurer(advId).GetMaxCommodity());
    }

    //type=21
    public void Op21(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int comId = scanner.nextInt();
        basicData.GetAdventurer(advId).CheckInstance(comId);
    }

    //type=22
    public void Op22(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        basicData.GetAdventurer(advId).SellTakeAll(basicData.GetShop());
    }

    //type=23
    public void Op23(Scanner scanner, BasicData basicData) {
        int advId = scanner.nextInt();
        int objId = scanner.nextInt();
        String objName = scanner.next();
        String type = scanner.next();

        Shop shop = basicData.GetShop();
        Adventurer adventurer = basicData.GetAdventurer(advId);

        if (type.equals("Food")) {
            Food food = new Food(objId, objName, shop.GetAveEnergy(), shop.GetAvePriceFood());
            adventurer.BuyFood(food);
        } else if (type.equals("RegularBottle")) {
            Bottle bottle = new RegularBottle(objId, objName,
                    shop.GetAveCapacity(), shop.GetAvePriceBottle());
            adventurer.BuyBottle(bottle);
        } else if (type.equals("ReinforcedBottle")) {
            double ratio = scanner.nextDouble();
            Bottle bottle = new ReinforcedBottle(objId, objName,
                    shop.GetAveCapacity(), shop.GetAvePriceBottle(), ratio);
            adventurer.BuyBottle(bottle);
        } else if (type.equals("RecoverBottle")) {
            double ratio = scanner.nextDouble();
            Bottle bottle = new RecoverBottle(objId, objName,
                    shop.GetAveCapacity(), shop.GetAvePriceBottle(), ratio);
            adventurer.BuyBottle(bottle);
        } else if (type.equals("RegularEquipment")) {
            Equipment equipment = new RegularEquipment(objId, objName,
                    shop.GetAveStar(), shop.GetAvePriceEquipment());
            adventurer.BuyEquipment(equipment);
        } else if (type.equals("CritEquipment")) {
            int critical = scanner.nextInt();
            Equipment equipment = new CritEquipment(objId, objName,
                    shop.GetAveStar(), shop.GetAvePriceEquipment(), critical);
            adventurer.BuyEquipment(equipment);
        } else if (type.equals("EpicEquipment")) {
            double ratio = scanner.nextDouble();
            Equipment equipment = new EpicEquipment(objId, objName,
                    shop.GetAveStar(), shop.GetAvePriceEquipment(), ratio);
            adventurer.BuyEquipment(equipment);
        }
    }
}