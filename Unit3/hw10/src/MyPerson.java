import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;

import java.util.HashMap;
import java.util.Map;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Person> acquaintances;
    private final HashMap<Integer, Integer> values;
    private final HashMap<Integer, Tag> tags;
    private int bestAcquaintanceId;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintances = new HashMap<>();
        this.values = new HashMap<>();
        this.tags = new HashMap<>();
        this.bestAcquaintanceId = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean containsTag(int id) {
        return this.tags.containsKey(id);
    }

    @Override
    public Tag getTag(int id) {
        return this.tags.get(id);
    }

    @Override
    public void addTag(Tag tag) {
        this.tags.put(tag.getId(), tag);
    }

    @Override
    public void delTag(int id) {
        this.tags.remove(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Person)) {
            return false;
        }

        return this.id == ((Person) obj).getId();
    }

    public boolean strictEquals(Person person) {
        return this.id == person.getId() &&
            this.name.equals(person.getName()) &&
            this.age == person.getAge();
    }

    @Override
    public boolean isLinked(Person person) {
        int personId = person.getId();
        return this.acquaintances.containsKey(personId) || personId == this.id;
    }

    @Override
    public int queryValue(Person person) {
        int personId = person.getId();
        if (this.values.containsKey(personId)) {
            return this.values.get(personId);
        }
        return 0;
    }

    public static void AddRelation(MyPerson person1, MyPerson person2, int value) {
        int id1 = person1.id;
        int id2 = person2.id;

        person1.acquaintances.put(id2, person2);
        person1.values.put(id2, value);

        person2.acquaintances.put(id1, person1);
        person2.values.put(id1, value);

        person1.SetBestId(id2, value);
        person2.SetBestId(id1, value);
    }

    public static void SetNewValue(MyPerson person1, MyPerson person2, int newValue) {
        if (newValue <= 0) {
            MyPerson.DeleteRelation(person1, person2);
        } else {
            person1.values.put(person2.id, newValue);
            person2.values.put(person1.id, newValue);

            person1.SetBestId(person2.id, newValue);
            person2.SetBestId(person1.id, newValue);
        }
    }

    private void SetBestId(int acquaintanceId, int value) {
        if (this.bestAcquaintanceId == acquaintanceId) {
            this.ResetBestId();
        } else {
            if (this.bestAcquaintanceId == this.id) {
                this.bestAcquaintanceId = acquaintanceId;
            } else {
                if (value > this.values.get(this.bestAcquaintanceId) ||
                    (value == this.values.get(this.bestAcquaintanceId)
                        && acquaintanceId < this.bestAcquaintanceId)) {
                    this.bestAcquaintanceId = acquaintanceId;
                }
            }
        }
    }

    public static void DeleteRelation(MyPerson person1, MyPerson person2) {
        int id1 = person1.id;
        int id2 = person2.id;

        person1.acquaintances.remove(id2);
        person1.values.remove(id2);

        person2.acquaintances.remove(id1);
        person2.values.remove(id1);

        if (id1 == person2.bestAcquaintanceId) {
            person2.ResetBestId();
        }
        if (id2 == person1.bestAcquaintanceId) {
            person1.ResetBestId();
        }
    }

    private void ResetBestId() {
        this.bestAcquaintanceId = this.id;
        int maxValue = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Integer> entry : this.values.entrySet()) {
            int id = entry.getKey();
            int value = entry.getValue();
            if (value > maxValue || (value == maxValue && id < this.bestAcquaintanceId)) {
                maxValue = value;
                this.bestAcquaintanceId = id;
            }
        }
    }

    public void DeleteFromSharedTag(Person person) {
        for (Map.Entry<Integer, Tag> entry : this.tags.entrySet()) {
            MyTag tag = (MyTag) entry.getValue();
            if (tag.hasPerson(person)) {
                tag.delPerson(person);
            }
        }
    }

    public int GetSharedAcquaintanceNum(Person otherPerson) {
        int count = 0;
        for (Map.Entry<Integer, Person> entry : this.acquaintances.entrySet()) {
            Person person = entry.getValue();
            if (person.isLinked(otherPerson) && !otherPerson.equals(person)) {
                count++;
            }
        }
        return count;
    }

    public HashMap<Integer, Person> GetAcquaintanceMap() {
        return this.acquaintances;
    }

    public boolean NotHaveAcquaintance() {
        return this.acquaintances.isEmpty();
    }

    public int GetBestId() {
        return this.bestAcquaintanceId;
    }
}
