import java.util.ArrayList;

public class Record {
    private String date;
    private int type;   //1:use bottle  2:one  3:more
    private int fighterId;
    private String fighterName;
    private int fightedId;
    private int useId;
    private String useName;
    private ArrayList<Integer> fightedList;

    public Record(String date, int type, int advId, String advName
            , int useID, String useName, BasicData data) {
        this.date = date;
        this.type = type;
        this.fighterId = advId;
        this.fighterName = advName;
        this.useId = useID;
        this.useName = useName;
        this.fightedList = new ArrayList<>();

        if (type == 3) {
            ArrayList<Adventurer> advList = data.GetAdventurersList();
            for (Adventurer fighted : advList) {
                if (fighted.GetId() != fighterId) {
                    fightedList.add(fighted.GetId());
                }
            }
        }
    }

    public Record(String date, int type, int advId, String advName,
                  int fightedId, int useID, String useName, BasicData data) {
        this.date = date;
        this.type = type;
        this.fighterId = advId;
        this.fighterName = advName;
        this.fightedId = fightedId;
        this.useId = useID;
        this.useName = useName;
        this.fightedList = new ArrayList<>();
    }

    public String GetDate() {
        return this.date;
    }

    public int GetType() {
        return this.type;
    }

    public int GetFighterId() {
        return this.fighterId;
    }

    public String GetFighterName() {
        return this.fighterName;
    }

    public String GetUseName() {
        return this.useName;
    }

    public int GetFightedId() {
        return this.fightedId;
    }

    public boolean IsFighted(int advId) {
        if (this.type == 2) {
            return this.fightedId == advId;
        } else if (this.type == 3) {
            return this.fightedList.contains(advId);
        }
        return false;
    }
}
