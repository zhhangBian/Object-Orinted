import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.EqualTagIdException;
import com.oocourse.spec2.exceptions.PathNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.TagIdNotFoundException;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;
import exceptions.MyAcquaintanceNotFoundException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyEqualTagIdException;
import exceptions.MyPathNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;
import exceptions.MyTagIdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons;
    private final DisJointSet disJointSet;
    private int blockCount;
    private int tripleCount;
    private int coupleCount;
    private final HashMap<Integer, ArrayList<Tag>> personInTags;

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.disJointSet = new DisJointSet();
        this.blockCount = 0;
        this.tripleCount = 0;
        this.coupleCount = 0;
        this.personInTags = new HashMap<>();
    }

    @Override
    public boolean containsPerson(int id) {
        return this.persons.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return this.persons.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int personId = person.getId();
        if (this.containsPerson(personId)) {
            throw new MyEqualPersonIdException(person.getId());
        }

        this.persons.put(personId, person);
        this.disJointSet.AddPerson(person);
        this.blockCount++;
        this.personInTags.put(personId, new ArrayList<>());
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
        PersonIdNotFoundException, EqualRelationException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }

        MyPerson person1 = (MyPerson) this.persons.get(id1);
        MyPerson person2 = (MyPerson) this.persons.get(id2);
        if (person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }

        int bestIdBefore1 = person1.GetBestId();
        int bestIdBefore2 = person2.GetBestId();
        MyPerson.AddRelation(person1, person2, value);

        this.ResetCoupleNum(id1, id2, bestIdBefore1, bestIdBefore2);
        this.blockCount += this.disJointSet.AddRelationship(id1, id2);
        this.tripleCount += person1.GetSharedAcquaintanceNum(person2);
        this.ModifyTagValue(id1, id2, 0, value);
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
        throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        }

        MyPerson person1 = (MyPerson) persons.get(id1);
        MyPerson person2 = (MyPerson) persons.get(id2);
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }

        int oldValue = person1.queryValue(person2);
        int newValue = oldValue + value;
        int bestIdBefore1 = person1.GetBestId();
        int bestIdBefore2 = person2.GetBestId();

        if (newValue > 0) {
            MyPerson.SetNewValue(person1, person2, newValue);
            this.ModifyTagValue(id1, id2, oldValue, newValue);
        } else {
            this.tripleCount -= person1.GetSharedAcquaintanceNum(person2);
            this.ModifyTagValue(id1, id2, oldValue, 0);
            this.DeleteRelation(person1, person2);
        }
        this.ResetCoupleNum(id1, id2, bestIdBefore1, bestIdBefore2);
    }

    private void ModifyTagValue(int id1, int id2, int oldValue, int newValue) {
        Person person1 = this.persons.get(id1);
        Person person2 = this.persons.get(id2);
        ArrayList<Tag> personTags = this.personInTags.get(id1);
        for (Tag tag : personTags) {
            if (tag.hasPerson(person1) && tag.hasPerson(person2)) {
                ((MyTag) tag).ModifyValue(oldValue, newValue);
            }
        }
    }

    private void DeleteRelation(MyPerson person1, MyPerson person2) {
        MyPerson.DeleteRelation(person1, person2);

        int id1 = person1.getId();
        int id2 = person2.getId();
        HashSet<Integer> visited = new HashSet<>();
        DfsFind(id1, id2, visited);
        if (!visited.contains(id2)) {
            this.blockCount++;
            this.ReBuildDisJointSet(id1);
            this.ReBuildDisJointSet(id2);
        }

        person1.DeleteFromSharedTag(person2);
        person2.DeleteFromSharedTag(person1);
    }

    private void DfsFind(int idNow, int targetId, HashSet<Integer> visited) {
        visited.add(idNow);
        HashMap<Integer, Person> acquaintanceMap =
            ((MyPerson) this.persons.get(idNow)).GetAcquaintanceMap();
        if (acquaintanceMap.containsKey(targetId)) {
            visited.add(targetId);
            return;
        }

        for (Map.Entry<Integer, Person> entry : acquaintanceMap.entrySet()) {
            int id = entry.getKey();
            if (!visited.contains(id)) {
                DfsFind(id, targetId, visited);
            }
        }
    }

    private void ReBuildDisJointSet(int id) {
        this.disJointSet.SetRank(id, 1);
        this.DfsReBuild(id, id, new HashSet<>());
    }

    private void DfsReBuild(int idNow, int targetId, HashSet<Integer> visited) {
        this.disJointSet.ReBuild(idNow, targetId);
        visited.add(idNow);

        for (Map.Entry<Integer, Person> entry :
            ((MyPerson) this.persons.get(idNow)).GetAcquaintanceMap().entrySet()) {
            int id = entry.getKey();
            if (!visited.contains(id)) {
                DfsReBuild(id, targetId, visited);
            }
        }
    }

    private void ResetCoupleNum(int id1, int id2, int bestIdBefore1, int bestIdBefore2) {
        MyPerson person1 = (MyPerson) this.persons.get(id1);
        MyPerson person2 = (MyPerson) this.persons.get(id2);

        if (bestIdBefore1 == id2 && bestIdBefore2 == id1) {
            this.coupleCount--;
        } else if (bestIdBefore1 == id2 && bestIdBefore2 != id1) {
            MyPerson personCouple = (MyPerson) this.persons.get(bestIdBefore2);
            if (personCouple.GetBestId() == id2) {
                this.coupleCount--;
            }
        } else if (bestIdBefore1 != id2 && bestIdBefore2 == id1) {
            MyPerson personCouple = (MyPerson) this.persons.get(bestIdBefore1);
            if (personCouple.GetBestId() == id1) {
                this.coupleCount--;
            }
        } else {
            MyPerson personCouple1 = (MyPerson) this.persons.get(bestIdBefore1);
            MyPerson personCouple2 = (MyPerson) this.persons.get(bestIdBefore2);

            if (personCouple1.GetBestId() == id1) {
                this.coupleCount--;
            }
            if (personCouple2.GetBestId() == id2) {
                this.coupleCount--;
            }
        }

        int bestIdNow1 = person1.GetBestId();
        int bestIdNow2 = person2.GetBestId();
        if (bestIdNow1 == id2 && bestIdNow2 == id1) {
            this.coupleCount++;
        } else if (bestIdNow1 == id2 && bestIdNow2 != id1) {
            MyPerson personCouple = (MyPerson) this.persons.get(bestIdNow2);
            if (personCouple.GetBestId() == id2) {
                this.coupleCount++;
            }
        } else if (bestIdNow1 != id2 && bestIdNow2 == id1) {
            MyPerson personCouple = (MyPerson) this.persons.get(bestIdNow1);
            if (personCouple.GetBestId() == id1) {
                this.coupleCount++;
            }
        } else {
            MyPerson personCouple1 = (MyPerson) this.persons.get(bestIdNow1);
            MyPerson personCouple2 = (MyPerson) this.persons.get(bestIdNow2);

            if (personCouple1.GetBestId() == id1 && bestIdNow1 != id1) {
                this.coupleCount++;
            }

            if (personCouple2.GetBestId() == id2 && bestIdNow2 != id2) {
                this.coupleCount++;
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws
        PersonIdNotFoundException, RelationNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }

        MyPerson person1 = (MyPerson) persons.get(id1);
        MyPerson person2 = (MyPerson) persons.get(id2);

        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }

        return person1.queryValue(person2);
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }

        return this.disJointSet.IsConnected(id1, id2);
    }

    @Override
    public int queryBlockSum() {
        return this.blockCount;
    }

    @Override
    public int queryTripleSum() {
        return this.tripleCount;
    }

    @Override
    public void addTag(int personId, Tag tag) throws
        PersonIdNotFoundException, EqualTagIdException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }

        Person person = this.persons.get(personId);
        if (person.containsTag(tag.getId())) {
            throw new MyEqualTagIdException(tag.getId());
        }

        person.addTag(tag);
        ((MyTag) tag).SetPersonId(personId);
    }

    @Override
    public void addPersonToTag(int personId1, int personId2, int tagId)
        throws PersonIdNotFoundException, RelationNotFoundException,
        TagIdNotFoundException, EqualPersonIdException {
        if (!this.containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        }
        if (!this.containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        }
        if (personId1 == personId2) {
            throw new MyEqualPersonIdException(personId1);
        }

        Person person1 = this.persons.get(personId1);
        Person person2 = this.persons.get(personId2);
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(personId1, personId2);
        }

        if (!person2.containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }

        Tag tag = person2.getTag(tagId);
        if (tag.hasPerson(person1)) {
            throw new MyEqualPersonIdException(personId1);
        }

        if (tag.getSize() <= 1111) {
            tag.addPerson(person1);
            this.personInTags.get(personId1).add(tag);
        }
    }

    @Override
    public int queryTagValueSum(int personId, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }

        Person person = this.persons.get(personId);
        if (!person.containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }

        return person.getTag(tagId).getValueSum();
    }

    @Override
    public int queryTagAgeVar(int personId, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }

        Person person = this.persons.get(personId);
        if (!person.containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }

        return person.getTag(tagId).getAgeVar();
    }

    @Override
    public void delPersonFromTag(int personId1, int personId2, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!this.containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        }
        if (!this.containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        }

        Person person1 = this.persons.get(personId1);
        Person person2 = this.persons.get(personId2);
        if (!person2.containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }

        Tag tag = person2.getTag(tagId);
        if (!tag.hasPerson(person1)) {
            throw new MyPersonIdNotFoundException(personId1);
        }

        tag.delPerson(person1);
        this.personInTags.get(personId1).remove(tag);
    }

    @Override
    public void delTag(int personId, int tagId)
        throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }

        Person person = this.persons.get(personId);
        if (!person.containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        }

        person.delTag(tagId);
    }

    @Override
    public int queryBestAcquaintance(int id)
        throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!this.containsPerson(id)) {
            throw new MyPersonIdNotFoundException(id);
        }

        MyPerson person = (MyPerson) this.persons.get(id);
        if (person.NotHaveAcquaintance()) {
            throw new MyAcquaintanceNotFoundException(id);
        }

        return person.GetBestId();
    }

    @Override
    public int queryCoupleSum() {
        return this.coupleCount;
    }

    @Override
    public int queryShortestPath(int id1, int id2)
        throws PersonIdNotFoundException, PathNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!this.isCircle(id1, id2)) {
            throw new MyPathNotFoundException(id1, id2);
        }

        return id1 == id2 ? 0 : BfsSearch(id1, id2) - 1;
    }

    private int BfsSearch(int idBegin, int idEnd) {
        ArrayList<Integer> queue = new ArrayList<>();
        HashMap<Integer, Integer> distanceMap = new HashMap<>();

        queue.add(idBegin);
        distanceMap.put(idBegin, 0);
        while (!queue.isEmpty()) {
            int idNow = queue.get(0);
            int fartherDistance = distanceMap.get(idNow);
            queue.remove(0);

            HashMap<Integer, Person> acquaintanceMap =
                ((MyPerson) this.persons.get(idNow)).GetAcquaintanceMap();
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

    public Person[] getPersons() {
        return this.persons.values().toArray(new Person[0]);
    }
}
