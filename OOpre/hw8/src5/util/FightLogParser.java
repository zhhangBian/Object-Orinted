package util;

import config.GlobalConfig;
import constants.AbstractBaseTypeEnum;
import constants.FightLogTypeEnum;
import constants.RegularExpression;
import entity.adventure.Adventurer;
import entity.prop.Prop;
import entity.fightlog.FightLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FightLogParser {

    private static final Pattern potionPattern =
            Pattern.compile(RegularExpression.POTION_PATTERN_STR);
    private static final Pattern singleAttackPattern =
            Pattern.compile(RegularExpression.SINGLE_ATTACK_PATTERN_STR);
    private static final Pattern aoeAttackPattern =
            Pattern.compile(RegularExpression.AOE_ATTACK_PATTERN_STR);

    public static void processFightLog(String fightLog, String[] adventurerInFightMode) {

        Matcher potionMatcher = potionPattern.matcher(fightLog);
        Matcher singleAttackMatcher = singleAttackPattern.matcher(fightLog);
        Matcher aoeAttackMatcher = aoeAttackPattern.matcher(fightLog);


        if (potionMatcher.find()) {
            potion(potionMatcher);
        } else if (singleAttackMatcher.find()) {
            singleAttack(singleAttackMatcher);
        } else if (aoeAttackMatcher.find()) {
            aoeAttack(aoeAttackMatcher, adventurerInFightMode);
        }
    }

    public static void potion(Matcher potionMatcher) {
        final String date = potionMatcher.group(1);
        String advName = potionMatcher.group(3);
        String botName = potionMatcher.group(4);
        Adventurer adventurer = GlobalConfig.ADVENTURERS.getAdventurerByName(advName);
        if (!adventurer.isFightMode()) {
            System.out.println("Fight log error");
            return;
        }
        Prop prop = adventurer.useProp(botName, AbstractBaseTypeEnum.BOTTLE);
        if (prop == null) {
            System.out.println("Fight log error");
            return;
        }
        System.out.printf("%d %d\n", prop.getId(), adventurer.getHitPoint());
        FightLog log = new FightLog(date, FightLogTypeEnum.POTION, false, advName,
                                    -1, -1, botName, null);
        GlobalConfig.FIGHT_LOGS.addFightLog(log);
    }

    public static void singleAttack(Matcher singleAttackMatcher) {
        final String date = singleAttackMatcher.group(1);
        String advName = singleAttackMatcher.group(3);
        String attackedName = singleAttackMatcher.group(4);
        String equName = singleAttackMatcher.group(5);
        Adventurer adventurerPos = GlobalConfig.ADVENTURERS.getAdventurerByName(advName);
        Adventurer adventurerNeg = GlobalConfig.ADVENTURERS.getAdventurerByName(attackedName);
        if (!adventurerPos.isFightMode() || !adventurerNeg.isFightMode()) {
            System.out.println("Fight log error");
            return;
        }
        Prop equ = adventurerPos.searchPropInBagByName(equName, AbstractBaseTypeEnum.EQUIPMENT);
        if (equ == null) {
            System.out.println("Fight log error");
            return;
        }
        adventurerPos.fight(adventurerNeg, equ);
        System.out.printf("%d %d\n", adventurerNeg.getId(), adventurerNeg.getHitPoint());
        FightLog log = new FightLog(date, FightLogTypeEnum.SINGLE_ATTACK, false, null,
                                    adventurerPos.getId(), adventurerNeg.getId(),
                                    null, equName);
        GlobalConfig.FIGHT_LOGS.addFightLog(log);
    }

    public static void aoeAttack(Matcher aoeAttackMatcher, String[] adventurerInFightMode) {
        final String date = aoeAttackMatcher.group(1);
        String advName = aoeAttackMatcher.group(3);
        String equName = aoeAttackMatcher.group(4);
        Adventurer adventurerPos = GlobalConfig.ADVENTURERS.getAdventurerByName(advName);
        if (!adventurerPos.isFightMode()) {
            System.out.println("Fight log error");
            return;
        }
        Prop equ = adventurerPos.searchPropInBagByName(equName, AbstractBaseTypeEnum.EQUIPMENT);
        if (equ == null) {
            System.out.println("Fight log error");
            return;
        }
        for (String attackedName : adventurerInFightMode) {
            Adventurer adventurerNeg = GlobalConfig.ADVENTURERS.getAdventurerByName(attackedName);
            if (adventurerNeg.equals(adventurerPos)) {
                continue;
            }
            adventurerPos.fight(adventurerNeg, equ);
            System.out.printf("%d ", adventurerNeg.getHitPoint());
            FightLog log = new FightLog(date, FightLogTypeEnum.AOE_ATTACK, false, null,
                    adventurerPos.getId(),adventurerNeg.getId(),
                    null, equName);
            GlobalConfig.FIGHT_LOGS.addFightLog(log);
        }
        FightLog log = new FightLog(date, FightLogTypeEnum.AOE_ATTACK, true, null,
                adventurerPos.getId(), -1,
                null, equName);
        GlobalConfig.FIGHT_LOGS.addFightLog(log);
        System.out.print("\n");
    }
}
