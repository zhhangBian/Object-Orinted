import java.util.ArrayList;
import java.util.Iterator;

public class Team {
    private ArrayList<Soldier> soldiers;

    public Team() {
        this.soldiers = new ArrayList<>();
    }

    public int GetTeamSize() {
        return this.soldiers.size();
    }

    public void addSoldier(Soldier soldier) {
        this.soldiers.add(soldier);
    }

    public void screen(int standard) {
        Iterator<Soldier> iterator = soldiers.iterator();
        while (iterator.hasNext()) {
            Soldier soldier = iterator.next();
            if (soldier.notQualifiedByStandard(standard)) {
                iterator.remove();
            }
        }
    }

    public void allAddStr(String str) {
        for (Soldier soldier : soldiers) {
            soldier.appendStr2Incantation(str);
        }
    }

    public void allIntercept(int a, int b) {
        for (Soldier soldier : soldiers) {
            soldier.cutIncantation(a, b);
        }
    }

    public Team cloneSelf() {
        Team team = new Team();
        for (Soldier soldier : soldiers) {
            team.addSoldier(soldier.cloneSoldier());
        }
        return team;
    }

    public void mergeTeam(Team team) {
        for (Soldier soldier : team.soldiers) {
            boolean repeat = false;
            for (Soldier oldSoldier : this.soldiers) {
                if (oldSoldier.equal(soldier)) {
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                this.soldiers.add(soldier);
            }
        }
    }

    public int getSizeOfHasStr(String str) {
        int count = 0;
        for (Soldier soldier : this.soldiers) {
            if (soldier.hasString(str)) {
                count++;
            }
        }
        return count;
    }
}