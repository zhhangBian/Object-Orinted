//import com.oocourse.spec2.main.Person;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class MyPersonTest {
//    Person person = new MyPerson(1, "2", 3);
//
//    @Test
//    public void getId() {
//        assertEquals(person.getId(), 1);
//    }
//
//    @Test
//    public void getName() {
//        assertEquals(person.getName(), "2");
//    }
//
//    @Test
//    public void getAge() {
//        assertEquals(person.getAge(), 3);
//    }
//
//    @Test
//    public void testEquals() {
//        Person person1 = new MyPerson(1, "2", 3);
//        Person person2 = new MyPerson(1, "3", 4);
//        Person person3 = new MyPerson(2, "2", 3);
//        assertTrue(person1.equals(person2));
//        assertTrue(person1.equals(person1));
//        assertFalse(person1.equals(person3));
//        assertFalse(person1.equals(new MyNetwork()));
//        assertFalse(person1.equals(null));
//    }
//
//    @Test
//    public void isLinked() {
//        MyPerson person1 = new MyPerson(1, "2", 3);
//        MyPerson person2 = new MyPerson(2, "2", 3);
//        MyPerson person3 = new MyPerson(3, "2", 3);
//
//        assertTrue(person1.isLinked(person1));
//
//        MyPerson.AddRelation(person1, person2, 1);
//        assertTrue(person1.isLinked(person2));
//        assertTrue(person2.isLinked(person1));
//        assertFalse(person1.isLinked(person3));
//
//        MyPerson.DeleteRelation(person1, person2);
//        assertFalse(person1.isLinked(person2));
//        assertFalse(person2.isLinked(person1));
//    }
//
//    @Test
//    public void queryValue() {
//        MyPerson person1 = new MyPerson(1, "2", 3);
//        MyPerson person2 = new MyPerson(2, "2", 3);
//        MyPerson person3 = new MyPerson(3, "2", 3);
//
//        MyPerson.AddRelation(person1, person2, 1);
//        assertEquals(person1.queryValue(person2), 1);
//        assertEquals(person2.queryValue(person1), 1);
//
//        assertEquals(person1.queryValue(person1), 0);
//        assertEquals(person1.queryValue(person3), 0);
//
//        MyPerson.SetNewValue(person1, person2, 3);
//        assertEquals(person1.queryValue(person2), 3);
//        assertEquals(person2.queryValue(person1), 3);
//    }
//
//    @Test
//    public void addRelation() {
//        MyPerson person1 = new MyPerson(1, "2", 3);
//        MyPerson person2 = new MyPerson(2, "2", 3);
//        MyPerson.AddRelation(person1, person2, 1);
//        assertTrue(person1.isLinked(person2));
//        assertTrue(person2.isLinked(person1));
//    }
//
//    @Test
//    public void modifyValue() {
//        MyPerson person1 = new MyPerson(1, "2", 3);
//        MyPerson person2 = new MyPerson(2, "2", 3);
//        MyPerson.AddRelation(person1, person2, 1);
//        assertEquals(person1.queryValue(person2), 1);
//        assertEquals(person2.queryValue(person1), 1);
//
//        MyPerson.SetNewValue(person1, person2, 10);
//        assertEquals(person1.queryValue(person2), 10);
//        assertEquals(person2.queryValue(person1), 10);
//
//        MyPerson.SetNewValue(person1, person2, 0);
//        assertFalse(person1.isLinked(person2));
//        MyPerson.SetNewValue(person1, person2, -5);
//        assertFalse(person1.isLinked(person2));
//    }
//
//    @Test
//    public void deleteRelationship() {
//
//    }
//
//    @Test
//    public void getSharedAcquaintanceNum() {
//        MyPerson person1 = new MyPerson(1, "2", 3);
//        MyPerson person2 = new MyPerson(2, "2", 3);
//        MyPerson person3 = new MyPerson(3, "2", 3);
//        MyPerson person4 = new MyPerson(4, "2", 3);
//        MyPerson person5 = new MyPerson(5, "2", 3);
//        MyPerson person6 = new MyPerson(6, "2", 3);
//        MyPerson person7 = new MyPerson(7, "2", 3);
//        MyPerson person8 = new MyPerson(8, "2", 3);
//        MyPerson person9 = new MyPerson(9, "2", 3);
//        MyPerson person10 = new MyPerson(10, "2", 3);
//
//        MyPerson.AddRelation(person1, person2, 1);
//
//        MyPerson.AddRelation(person1, person3, 1);
//        MyPerson.AddRelation(person2, person3, 1);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 1);
//        MyPerson.AddRelation(person1, person4, 1);
//        MyPerson.AddRelation(person2, person4, 1);
//        assertEquals(person2.GetSharedAcquaintanceNum(person1), 2);
//        MyPerson.AddRelation(person1, person5, 1);
//        MyPerson.AddRelation(person2, person5, 1);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 3);
//        MyPerson.AddRelation(person1, person6, 1);
//        MyPerson.AddRelation(person2, person6, 1);
//        assertEquals(person2.GetSharedAcquaintanceNum(person1), 4);
//        MyPerson.AddRelation(person1, person7, 1);
//        MyPerson.AddRelation(person2, person7, 1);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 5);
//        MyPerson.AddRelation(person1, person8, 1);
//        MyPerson.AddRelation(person2, person8, 1);
//        assertEquals(person2.GetSharedAcquaintanceNum(person1), 6);
//        MyPerson.AddRelation(person1, person9, 1);
//        MyPerson.AddRelation(person2, person9, 1);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 7);
//        MyPerson.AddRelation(person1, person10, 1);
//        MyPerson.AddRelation(person2, person10, 1);
//        assertEquals(person2.GetSharedAcquaintanceNum(person1), 8);
//
//        MyPerson.DeleteRelation(person1, person3);
//        MyPerson.DeleteRelation(person2, person3);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 7);
//        MyPerson.DeleteRelation(person1, person4);
//        MyPerson.DeleteRelation(person2, person4);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 6);
//        MyPerson.DeleteRelation(person1, person5);
//        MyPerson.DeleteRelation(person2, person5);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 5);
//        MyPerson.DeleteRelation(person1, person6);
//        MyPerson.DeleteRelation(person2, person6);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 4);
//        MyPerson.DeleteRelation(person1, person7);
//        MyPerson.DeleteRelation(person2, person7);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 3);
//        MyPerson.DeleteRelation(person1, person8);
//        MyPerson.DeleteRelation(person2, person8);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 2);
//        MyPerson.DeleteRelation(person1, person9);
//        MyPerson.DeleteRelation(person2, person9);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 1);
//        MyPerson.DeleteRelation(person1, person10);
//        MyPerson.DeleteRelation(person2, person10);
//        assertEquals(person1.GetSharedAcquaintanceNum(person2), 0);
//    }
//
//    @Test
//    public void haveSharedAcquaintance() {
//        MyPerson person1 = new MyPerson(1, "2", 3);
//        MyPerson person2 = new MyPerson(2, "2", 3);
//        MyPerson person3 = new MyPerson(3, "2", 3);
//
//        MyPerson.AddRelation(person1, person2, 1);
//        assertFalse(person1.GetSharedAcquaintanceNum(person2) != 0);
//
//        MyPerson.AddRelation(person1, person3, 1);
//        MyPerson.AddRelation(person2, person3, 1);
//        assertTrue(person1.GetSharedAcquaintanceNum(person2) != 0);
//    }
//
//    @Test
//    public void getAcquaintanceMap() {
//        MyPerson person = new MyPerson(1, "2", 3);
//        person.GetAcquaintanceMap();
//    }
//}
