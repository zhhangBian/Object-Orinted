import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class DisJointSet {
    private final HashMap<Integer, Integer> representPersonMap;
    private final HashMap<Integer, Integer> rankMap;

    public DisJointSet() {
        this.representPersonMap = new HashMap<>();
        this.rankMap = new HashMap<>();
    }

    public void AddPerson(Person person) {
        this.representPersonMap.put(person.getId(), person.getId());
        this.rankMap.put(person.getId(), 0);
    }

    public int FindRepresentId(int id) {
        int representId = id;
        while (representId != this.representPersonMap.get(representId)) {
            representId = this.representPersonMap.get(representId);
        }

        int now = id;
        while (now != representId) {
            int fartherId = this.representPersonMap.get(now);
            this.representPersonMap.put(now, representId);
            now = fartherId;
        }

        return representId;
    }

    public int AddRelationship(int id1, int id2) {
        int representId1 = this.FindRepresentId(id1);
        int representId2 = this.FindRepresentId(id2);

        if (representId1 == representId2) {
            return 0;
        }

        int rank1 = this.rankMap.get(representId1);
        int rank2 = this.rankMap.get(representId2);

        if (rank1 < rank2) {
            this.representPersonMap.put(representId1, representId2);
        } else if (rank1 == rank2) {
            this.rankMap.put(representId1, rank1 + 1);
            this.representPersonMap.put(representId2, representId1);
        } else {
            this.representPersonMap.put(representId2, representId1);
        }

        return -1;
    }

    public boolean IsConnected(int id1, int id2) {
        return this.FindRepresentId(id1) == this.FindRepresentId(id2);
    }

    public void ReBuild(int idNow, int targetId) {
        this.representPersonMap.put(idNow, targetId);
    }

    public void SetRank(int id, int rank) {
        this.rankMap.put(id, rank);
    }
}
