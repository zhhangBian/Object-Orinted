import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;
import exceptions.MyEqualPersonIdException;
import exceptions.MyRelationNotFoundException;
import exceptions.MyTagIdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class NetworkMethods {
    public static void ModifyTagValue(Person person1, Person person2,
                                      int oldValue, int newValue, ArrayList<Tag> personTags) {
        for (Tag tag : personTags) {
            if (tag.hasPerson(person1) && tag.hasPerson(person2)) {
                ((MyTag) tag).ModifyValue(oldValue, newValue);
            }
        }
    }

    public static void DfsFindPerson(int idNow, int targetId,
                                     HashSet<Integer> visited,
                                     HashMap<Integer, Person> persons) {
        visited.add(idNow);
        HashMap<Integer, Person> acquaintanceMap =
            ((MyPerson) persons.get(idNow)).GetAcquaintanceMap();
        if (acquaintanceMap.containsKey(targetId)) {
            visited.add(targetId);
            return;
        }

        for (Map.Entry<Integer, Person> entry : acquaintanceMap.entrySet()) {
            int id = entry.getKey();
            if (!visited.contains(id)) {
                DfsFindPerson(id, targetId, visited, persons);
            }
        }
    }

    public static int DeleteRelation(MyPerson person1,
                                     MyPerson person2,
                                     DisJointSet disJointSet,
                                     HashMap<Integer, Person> persons) {
        int add = 0;
        MyPerson.DeleteRelation(person1, person2);

        int id1 = person1.getId();
        int id2 = person2.getId();
        HashSet<Integer> visited = new HashSet<>();
        NetworkMethods.DfsFindPerson(id1, id2, visited, persons);
        if (!visited.contains(id2)) {
            add = 1;
            ReBuildDisJointSet(id1, disJointSet, persons);
            ReBuildDisJointSet(id2, disJointSet, persons);
        }

        person1.DeleteFromSharedTag(person2);
        person2.DeleteFromSharedTag(person1);

        return add;
    }

    public static void ReBuildDisJointSet(int id,
                                          DisJointSet disJointSet,
                                          HashMap<Integer, Person> persons) {
        disJointSet.SetRank(id, 1);
        DfsReBuild(id, id, new HashSet<>(), disJointSet, persons);
    }

    public static void DfsReBuild(int idNow, int targetId,
                                  HashSet<Integer> visited,
                                  DisJointSet disJointSet,
                                  HashMap<Integer, Person> persons) {
        disJointSet.ReBuild(idNow, targetId);
        visited.add(idNow);

        for (Map.Entry<Integer, Person> entry :
            ((MyPerson) persons.get(idNow)).GetAcquaintanceMap().entrySet()) {
            int id = entry.getKey();
            if (!visited.contains(id)) {
                DfsReBuild(id, targetId, visited, disJointSet, persons);
            }
        }
    }

    public static int ResetCoupleNum(int id1, int id2, int bestIdBefore1, int bestIdBefore2,
                                     HashMap<Integer, Person> persons,
                                     int oldCoupleCount) {
        int coupleCount = oldCoupleCount;
        MyPerson person1 = (MyPerson) persons.get(id1);
        MyPerson person2 = (MyPerson) persons.get(id2);

        if (bestIdBefore1 == id2 && bestIdBefore2 == id1) {
            coupleCount--;
        } else if (bestIdBefore1 == id2 && bestIdBefore2 != id1) {
            MyPerson personCouple = (MyPerson) persons.get(bestIdBefore2);
            if (personCouple.GetBestId() == id2) {
                coupleCount--;
            }
        } else if (bestIdBefore1 != id2 && bestIdBefore2 == id1) {
            MyPerson personCouple = (MyPerson) persons.get(bestIdBefore1);
            if (personCouple.GetBestId() == id1) {
                coupleCount--;
            }
        } else {
            MyPerson personCouple1 = (MyPerson) persons.get(bestIdBefore1);
            MyPerson personCouple2 = (MyPerson) persons.get(bestIdBefore2);

            if (personCouple1.GetBestId() == id1) {
                coupleCount--;
            }
            if (personCouple2.GetBestId() == id2) {
                coupleCount--;
            }
        }

        int bestIdNow1 = person1.GetBestId();
        int bestIdNow2 = person2.GetBestId();
        if (bestIdNow1 == id2 && bestIdNow2 == id1) {
            coupleCount++;
        } else if (bestIdNow1 == id2 && bestIdNow2 != id1) {
            MyPerson personCouple = (MyPerson) persons.get(bestIdNow2);
            if (personCouple.GetBestId() == id2) {
                coupleCount++;
            }
        } else if (bestIdNow1 != id2 && bestIdNow2 == id1) {
            MyPerson personCouple = (MyPerson) persons.get(bestIdNow1);
            if (personCouple.GetBestId() == id1) {
                coupleCount++;
            }
        } else {
            MyPerson personCouple1 = (MyPerson) persons.get(bestIdNow1);
            MyPerson personCouple2 = (MyPerson) persons.get(bestIdNow2);

            if (personCouple1.GetBestId() == id1 && bestIdNow1 != id1) {
                coupleCount++;
            }

            if (personCouple2.GetBestId() == id2 && bestIdNow2 != id2) {
                coupleCount++;
            }
        }

        return coupleCount;
    }

    public static int BfsSearch(int idBegin, int idEnd, HashMap<Integer, Person> persons) {
        ArrayList<Integer> queue = new ArrayList<>();
        HashMap<Integer, Integer> distanceMap = new HashMap<>();

        queue.add(idBegin);
        distanceMap.put(idBegin, 0);
        while (!queue.isEmpty()) {
            int idNow = queue.get(0);
            int fartherDistance = distanceMap.get(idNow);
            queue.remove(0);

            HashMap<Integer, Person> acquaintanceMap =
                ((MyPerson) persons.get(idNow)).GetAcquaintanceMap();
            if (acquaintanceMap.containsKey(idEnd)) {
                return fartherDistance + 1;
            }

            for (Map.Entry<Integer, Person> entry : acquaintanceMap.entrySet()) {
                int id = entry.getKey();
                if (!distanceMap.containsKey(id)) {
                    distanceMap.put(id, fartherDistance + 1);
                    queue.add(id);
                }
            }
        }

        return 0;
    }

    public static void ThrowEqualPersonId(boolean bool, int id) throws MyEqualPersonIdException {
        if (bool) {
            throw new MyEqualPersonIdException(id);
        }
    }

    public static void CheckHaveRelationship(Person person1, Person person2)
        throws MyRelationNotFoundException {
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(person1.getId(), person2.getId());
        }
    }

    public static void ThrowTagIdNotFound(boolean bool, int id) throws MyTagIdNotFoundException {
        if (bool) {
            throw new MyTagIdNotFoundException(id);
        }
    }
}
