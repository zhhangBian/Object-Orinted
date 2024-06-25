package entity.fightlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FightLogs {

    private final List<FightLog> fightLogs = new ArrayList<>();

    private final HashMap<String, List<FightLog>> fightLogDateIndex = new HashMap<>();

    private final HashMap<Integer, List<FightLog>> fightLogPosIdIndex = new HashMap<>();

    private final HashMap<Integer, List<FightLog>> fightLogNegIdIndex = new HashMap<>();

    public void addFightLog(FightLog log) {
        fightLogs.add(log);
        String date = log.getDate();
        int posId = log.getPositiveId();
        int negId = log.getNegativeId();
        if (!fightLogDateIndex.containsKey(date)) {
            fightLogDateIndex.put(date, new ArrayList<>());
        }
        if (!fightLogPosIdIndex.containsKey(posId)) {
            fightLogPosIdIndex.put(posId, new ArrayList<>());
        }
        if (!fightLogNegIdIndex.containsKey(negId)) {
            fightLogNegIdIndex.put(negId, new ArrayList<>());
        }
        fightLogDateIndex.get(date).add(log);
        fightLogPosIdIndex.get(posId).add(log);
        fightLogNegIdIndex.get(negId).add(log);
    }

    public List<FightLog> getFightLogByDate(String date) {
        List<FightLog> logs;
        if (!fightLogDateIndex.containsKey(date)) {
            return null;
        }
        logs = fightLogDateIndex.get(date);
        return logs;
    }

    public List<FightLog> getFightLogsByPosId(int posId) {
        List<FightLog> logs;
        if (!fightLogPosIdIndex.containsKey(posId)) {
            return null;
        }
        logs = fightLogPosIdIndex.get(posId);
        return logs;
    }

    public List<FightLog> getFightLogsByNegId(int negId) {
        List<FightLog> logs;
        if (!fightLogNegIdIndex.containsKey(negId)) {
            return null;
        }
        logs = fightLogNegIdIndex.get(negId);
        return logs;
    }
}
