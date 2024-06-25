import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOhandle {

    public void HandleType(Scanner scanner, BasicData data, int type) {
        HashMap<Integer, Adventurer> adventurerHashMap = data.GetHashMap();

        switch (type) {
            case 1:
                this.AddAdventurer(scanner, adventurerHashMap);
                break;
            case 2:
                this.AddBottle(scanner, adventurerHashMap);
                break;
            case 3:
                this.DeleteBottle(scanner, adventurerHashMap);
                break;
            case 4:
                this.AddEquipment(scanner, adventurerHashMap);
                break;
            case 5:
                this.DeleteEquipment(scanner, adventurerHashMap);
                break;
            case 6:
                this.AddStar(scanner, adventurerHashMap);
                break;
            case 7:
                this.AddFood(scanner, adventurerHashMap);
                break;
            case 8:
                this.DeleteFood(scanner, adventurerHashMap);
                break;
            case 9:
                this.TakeEquipment(scanner, adventurerHashMap);
                break;
            case 10:
                this.TakeBottle(scanner, adventurerHashMap);
                break;
            case 11:
                this.TakeFood(scanner, adventurerHashMap);
                break;
            case 12:
                this.UseBottle(scanner, adventurerHashMap);
                break;
            case 13:
                this.UseFood(scanner, adventurerHashMap);
                break;
            case 14:
                this.EnterIssue(scanner, data);
                break;
            case 15:
                this.CheckFightDate(scanner, data);
                break;
            case 16:
                this.CheckFighter(scanner, data);
                break;
            case 17:
                this.CheckFighted(scanner, data);
                break;
            default:
                break;
        }
    }

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
        adventurer.UseBottle(botName, false);
    }

    //type=13
    public void UseFood(Scanner scanner, HashMap<Integer, Adventurer> adventurerHashMap) {
        int advId = scanner.nextInt();
        String foodName = scanner.next();

        Adventurer adventurer = adventurerHashMap.get(advId);
        adventurer.UseFood(foodName);
    }

    //type=14
    public void EnterIssue(Scanner scanner, BasicData data) {
        int m = scanner.nextInt();
        int k = scanner.nextInt();
        HashMap<Integer, Adventurer> adventurerHashMap = data.GetHashMap();

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
            String advname = scanner.next();
            int advId = data.GetAdventurerId(advname);
            Adventurer adventurer = adventurerHashMap.get(advId);
            adventurer.InFight();
            data.AddAdventurerInFight(adventurer);
        }

        for (int i = 0; i < k; i++) {
            String issue = scanner.next();

            Matcher matcherBottle = patternBottle.matcher(issue);
            Matcher matcherFightOne = patternFightOne.matcher(issue);
            Matcher matcherFightMore = patternFightMore.matcher(issue);

            if (matcherBottle.find()) {
                this.FightUseBottle(data, matcherBottle);
            } else if (matcherFightOne.find()) {
                this.FightOne(data, matcherFightOne);
            } else if (matcherFightMore.find()) {
                this.FightMore(data, matcherFightMore);
            }
        }
        data.OutFightAll(adventurerHashMap);
    }

    public void FightUseBottle(BasicData data, Matcher matcher) {
        String date = matcher.group(1);
        String advName = matcher.group(6);
        String botName = matcher.group(7);
        Adventurer adventurer = data.GetAdventurer(advName);

        if (adventurer.GetStatus() && adventurer.CheckTakeBottle(botName)) {
            data.AddRecord(date, 1, adventurer.GetId(), advName,
                    adventurer.GetBottleId(botName), botName, data);
            adventurer.UseBottle(botName, true);
        } else {
            System.out.println("Fight log error");
        }
    }

    public void FightOne(BasicData data, Matcher matcher) {
        String date = matcher.group(1);
        String fighterName = matcher.group(6);
        String fightedName = matcher.group(7);
        String equName = matcher.group(8);

        Adventurer fighter = data.GetAdventurer(fighterName);
        Adventurer fighted = data.GetAdventurer(fightedName);

        if (fighter.GetStatus() && fighted.GetStatus()
                && fighter.CheckTakeEquipment(equName)) {
            Equipment equipment = fighter.GetEquipment(equName);
            data.AddRecord(date, 2, fighter.GetId(), fighterName, fighted.GetId(),
                    equipment.GetId(), equName, data);
            fighted.GetAttacked(fighter.GetLevel(), equipment.GetStar());

            System.out.println(fighted.GetId() + " " + fighted.GetHitPoint());
        } else {
            System.out.println("Fight log error");
        }
    }

    public void FightMore(BasicData data, Matcher matcher) {
        String date = matcher.group(1);
        String advName = matcher.group(6);
        String equName = matcher.group(7);

        Adventurer fighter = data.GetAdventurer(advName);
        if (fighter.GetStatus() && fighter.CheckTakeEquipment(equName)) {
            Equipment equipment = fighter.GetEquipment(equName);
            ArrayList<Adventurer> advList = data.GetAdventurersList();
            data.AddRecord(date, 3, fighter.GetId(), advName, equipment.GetId(), equName, data);

            for (Adventurer fighted : advList) {
                if (!fighted.equals(fighter)) {
                    fighted.GetAttacked(fighter.GetLevel(), equipment.GetStar());
                    System.out.print(fighted.GetHitPoint() + " ");
                }
            }
            System.out.println();
        } else {
            System.out.println("Fight log error");
        }
    }

    //type=15
    public void CheckFightDate(Scanner scanner, BasicData data) {
        String date = scanner.next();
        data.CheckRecordDate(date);
    }

    //type=16
    public void CheckFighter(Scanner scanner, BasicData data) {
        int advId = scanner.nextInt();
        data.CheckRecordFighter(advId);
    }

    //type=17
    public void CheckFighted(Scanner scanner, BasicData data) {
        int advId = scanner.nextInt();
        data.CheckRecordFighted(advId);
    }
}