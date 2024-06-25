import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

import java.util.HashMap;
import java.util.Map;

public class MyTag implements Tag {
    private final int id;
    private int personId;
    private final HashMap<Integer, Person> persons;
    private int ageSum;
    private int ageSquareSum;
    private int valueSum;

    public MyTag(int id) {
        this.id = id;
        this.persons = new HashMap<>();
        this.ageSum = 0;
        this.ageSquareSum = 0;
        this.valueSum = 0;
    }

    public void SetPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }

        MyTag tag = (MyTag) obj;
        return this.id == tag.id && this.personId == tag.personId;
    }

    @Override
    public void addPerson(Person person) {
        this.persons.put(person.getId(), person);
        this.ageSum += person.getAge();
        this.ageSquareSum += person.getAge() * person.getAge();

        for (Map.Entry<Integer, Person> entry : this.persons.entrySet()) {
            Person personIn = entry.getValue();
            this.valueSum += 2 * person.queryValue(personIn);
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return this.persons.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return this.valueSum;
    }

    @Override
    public int getAgeMean() {
        return this.persons.isEmpty() ? 0 : (this.ageSum / this.persons.size());
    }

    @Override
    public int getAgeVar() {
        int ageVar = 0;
        if (!this.persons.isEmpty()) {
            int ageMean = this.getAgeMean();
            int size = this.persons.size();
            ageVar = (this.ageSquareSum - 2 * ageMean * this.ageSum +
                size * ageMean * ageMean) / size;
        }

        return ageVar;
    }

    @Override
    public void delPerson(Person person) {
        this.persons.remove(person.getId());
        this.ageSum -= person.getAge();
        this.ageSquareSum -= person.getAge() * person.getAge();

        for (Map.Entry<Integer, Person> entry : this.persons.entrySet()) {
            Person personIn = entry.getValue();
            this.valueSum -= 2 * person.queryValue(personIn);
        }
    }

    @Override
    public int getSize() {
        return this.persons.size();
    }

    public void ModifyValue(int oldValue, int newValue) {
        this.valueSum = this.valueSum - 2 * oldValue + 2 * newValue;
    }

    public void AddSocialValue(int socialValue) {
        this.persons.forEach((key, value) -> value.addSocialValue(socialValue));
    }

    public void AddMoney(int money) {
        this.persons.forEach((key, value) -> value.addMoney(money));
    }
}
