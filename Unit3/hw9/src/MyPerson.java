import com.oocourse.spec1.main.Person;

import java.util.HashMap;
import java.util.Map;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Person> acquaintanceMap;
    private final HashMap<Integer, Integer> valueMap;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintanceMap = new HashMap<>();
        this.valueMap = new HashMap<>();
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
        return this.id == person.getId()
            && this.name.equals(person.getName())
            && this.age == person.getAge();
    }

    @Override
    public boolean isLinked(Person person) {
        int personId = person.getId();
        return this.acquaintanceMap.containsKey(personId) || personId == this.id;
    }

    @Override
    public int queryValue(Person person) {
        int personId = person.getId();
        if (this.valueMap.containsKey(personId)) {
            return this.valueMap.get(personId);
        }
        return 0;
    }

    public static void AddRelation(MyPerson person1, MyPerson person2, int value) {
        int id1 = person1.id;
        int id2 = person2.id;

        person1.acquaintanceMap.put(id2, person2);
        person1.valueMap.put(id2, value);

        person2.acquaintanceMap.put(id1, person1);
        person2.valueMap.put(id1, value);
    }

    public static void SetNewValue(MyPerson person1, MyPerson person2, int newValue) {
        if (newValue <= 0) {
            MyPerson.DeleteRelation(person1, person2);
        } else {
            person1.valueMap.put(person2.id, newValue);
            person2.valueMap.put(person1.id, newValue);
        }
    }

    public static void DeleteRelation(MyPerson person1, MyPerson person2) {
        int id1 = person1.id;
        int id2 = person2.id;

        person1.acquaintanceMap.remove(id2);
        person1.valueMap.remove(id2);

        person2.acquaintanceMap.remove(id1);
        person2.valueMap.remove(id1);
    }

    public int GetSharedAcquaintanceNum(Person otherPerson) {
        int count = 0;
        for (Map.Entry<Integer, Person> entry : this.acquaintanceMap.entrySet()) {
            Person person = entry.getValue();
            if (person.isLinked(otherPerson) && !otherPerson.equals(person)) {
                count++;
            }
        }
        return count;
    }

    public HashMap<Integer, Person> GetAcquaintanceMap() {
        return this.acquaintanceMap;
    }
}
