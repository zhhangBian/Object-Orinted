import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons;
    private final DisJointSet disJointSet;
    private int blockCount;
    private int tripleCount;

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.disJointSet = new DisJointSet();
        this.blockCount = 0;
        this.tripleCount = 0;
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

        MyPerson.AddRelation(person1, person2, value);

        this.blockCount += this.disJointSet.AddRelationship(id1, id2);
        this.tripleCount += person1.GetSharedAcquaintanceNum(person2);
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

        if (newValue > 0) {
            MyPerson.SetNewValue(person1, person2, newValue);
        } else {
            this.tripleCount -= person1.GetSharedAcquaintanceNum(person2);

            MyPerson.DeleteRelation(person1, person2);
            HashSet<Integer> visited = new HashSet<>();
            DfsFind(id1, id2, visited);
            if (!visited.contains(id2)) {
                this.blockCount++;
                this.ReBuildDisJointSet(id1);
                this.ReBuildDisJointSet(id2);
            }
        }
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

    public Person[] getPersons() {
        return this.persons.values().toArray(new Person[0]);
    }
}
