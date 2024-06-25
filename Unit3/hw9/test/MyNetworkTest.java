import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyNetworkTest {
    @Test
    public void containsPerson() throws EqualPersonIdException {
        MyNetwork network = new MyNetwork();
        MyPerson person = new MyPerson(1, "2", 3);
        assertFalse(network.containsPerson(1));
        network.addPerson(person);
        assertTrue(network.containsPerson(1));
    }

    @Test
    public void getPerson() throws EqualPersonIdException {
        MyNetwork network = new MyNetwork();
        MyPerson person = new MyPerson(1, "2", 3);
        assertEquals(network.getPerson(1), null);
        network.addPerson(person);
        assertEquals(network.getPerson(1), person);
    }

    @Test
    public void addPerson() throws EqualPersonIdException {
        MyNetwork network = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "2", 3);
        MyPerson person2 = new MyPerson(2, "2", 3);

        network.addPerson(person1);
        network.addPerson(person2);
        assertEquals(network.getPerson(1), person1);
        assertEquals(network.getPerson(2), person2);
    }

    @Test
    public void addRelation() {
    }

    @Test
    public void modifyRelation() throws EqualPersonIdException,
        PersonIdNotFoundException, EqualRelationException, RelationNotFoundException {
        MyNetwork network = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "1", 1);
        MyPerson person2 = new MyPerson(2, "1", 1);

        network.addPerson(person1);
        network.addPerson(person2);

        int value = 1;
        network.addRelation(1, 2, value);
        assertEquals(network.queryValue(1, 2), value);

        for (int i = 0; i < 100; i++) {
            int dif = (int) (Math.random() * 100);
            network.modifyRelation(1, 2, dif);
            value += dif;
            assertEquals(network.queryValue(1, 2), value);
        }
    }

    @Test
    public void queryValue() {
    }

    @Test
    public void isCircle() throws PersonIdNotFoundException, EqualRelationException,
        EqualPersonIdException {
        MyNetwork network = new MyNetwork();
        MyPerson person0 = new MyPerson(0, "1", 1);
        network.addPerson(person0);
        assertTrue(network.isCircle(0, 0));

        {
            MyPerson person1 = new MyPerson(1, "1", 1);
            network.addPerson(person1);
            MyPerson person2 = new MyPerson(2, "1", 1);
            network.addPerson(person2);
            MyPerson person3 = new MyPerson(3, "1", 1);
            network.addPerson(person3);
            MyPerson person4 = new MyPerson(4, "1", 1);
            network.addPerson(person4);
            MyPerson person5 = new MyPerson(5, "1", 1);
            network.addPerson(person5);
            MyPerson person6 = new MyPerson(6, "1", 1);
            network.addPerson(person6);
            MyPerson person7 = new MyPerson(7, "1", 1);
            network.addPerson(person7);
            MyPerson person8 = new MyPerson(8, "1", 1);
            network.addPerson(person8);
            MyPerson person9 = new MyPerson(9, "1", 1);
            network.addPerson(person9);
            MyPerson person10 = new MyPerson(10, "1", 1);
            network.addPerson(person10);
            MyPerson person11 = new MyPerson(11, "1", 1);
            network.addPerson(person11);
            MyPerson person12 = new MyPerson(12, "1", 1);
            network.addPerson(person12);
            MyPerson person13 = new MyPerson(13, "1", 1);
            network.addPerson(person13);
            MyPerson person14 = new MyPerson(14, "1", 1);
            network.addPerson(person14);
            MyPerson person15 = new MyPerson(15, "1", 1);
            network.addPerson(person15);
            MyPerson person16 = new MyPerson(16, "1", 1);
            network.addPerson(person16);
            MyPerson person17 = new MyPerson(17, "1", 1);
            network.addPerson(person17);
            MyPerson person18 = new MyPerson(18, "1", 1);
            network.addPerson(person18);
            MyPerson person19 = new MyPerson(19, "1", 1);
            network.addPerson(person19);
            MyPerson person20 = new MyPerson(20, "1", 1);
            network.addPerson(person20);
            MyPerson person21 = new MyPerson(21, "1", 1);
            network.addPerson(person21);
            MyPerson person22 = new MyPerson(22, "1", 1);
            network.addPerson(person22);
            MyPerson person23 = new MyPerson(23, "1", 1);
            network.addPerson(person23);
            MyPerson person24 = new MyPerson(24, "1", 1);
            network.addPerson(person24);
            MyPerson person25 = new MyPerson(25, "1", 1);
            network.addPerson(person25);
            MyPerson person26 = new MyPerson(26, "1", 1);
            network.addPerson(person26);
            MyPerson person27 = new MyPerson(27, "1", 1);
            network.addPerson(person27);
            MyPerson person28 = new MyPerson(28, "1", 1);
            network.addPerson(person28);
            MyPerson person29 = new MyPerson(29, "1", 1);
            network.addPerson(person29);
            MyPerson person30 = new MyPerson(30, "1", 1);
            network.addPerson(person30);
            MyPerson person31 = new MyPerson(31, "1", 1);
            network.addPerson(person31);
            MyPerson person32 = new MyPerson(32, "1", 1);
            network.addPerson(person32);
            MyPerson person33 = new MyPerson(33, "1", 1);
            network.addPerson(person33);
            MyPerson person34 = new MyPerson(34, "1", 1);
            network.addPerson(person34);
            MyPerson person35 = new MyPerson(35, "1", 1);
            network.addPerson(person35);
            MyPerson person36 = new MyPerson(36, "1", 1);
            network.addPerson(person36);
            MyPerson person37 = new MyPerson(37, "1", 1);
            network.addPerson(person37);
            MyPerson person38 = new MyPerson(38, "1", 1);
            network.addPerson(person38);
            MyPerson person39 = new MyPerson(39, "1", 1);
            network.addPerson(person39);
            MyPerson person40 = new MyPerson(40, "1", 1);
            network.addPerson(person40);
            MyPerson person41 = new MyPerson(41, "1", 1);
            network.addPerson(person41);
            MyPerson person42 = new MyPerson(42, "1", 1);
            network.addPerson(person42);
            MyPerson person43 = new MyPerson(43, "1", 1);
            network.addPerson(person43);
            MyPerson person44 = new MyPerson(44, "1", 1);
            network.addPerson(person44);
            MyPerson person45 = new MyPerson(45, "1", 1);
            network.addPerson(person45);
            MyPerson person46 = new MyPerson(46, "1", 1);
            network.addPerson(person46);
            MyPerson person47 = new MyPerson(47, "1", 1);
            network.addPerson(person47);
            MyPerson person48 = new MyPerson(48, "1", 1);
            network.addPerson(person48);
            MyPerson person49 = new MyPerson(49, "1", 1);
            network.addPerson(person49);
            MyPerson person50 = new MyPerson(50, "1", 1);
            network.addPerson(person50);
            MyPerson person51 = new MyPerson(51, "1", 1);
            network.addPerson(person51);
            MyPerson person52 = new MyPerson(52, "1", 1);
            network.addPerson(person52);
            MyPerson person53 = new MyPerson(53, "1", 1);
            network.addPerson(person53);
            MyPerson person54 = new MyPerson(54, "1", 1);
            network.addPerson(person54);
            MyPerson person55 = new MyPerson(55, "1", 1);
            network.addPerson(person55);
            MyPerson person56 = new MyPerson(56, "1", 1);
            network.addPerson(person56);
            MyPerson person57 = new MyPerson(57, "1", 1);
            network.addPerson(person57);
            MyPerson person58 = new MyPerson(58, "1", 1);
            network.addPerson(person58);
            MyPerson person59 = new MyPerson(59, "1", 1);
            network.addPerson(person59);
            MyPerson person60 = new MyPerson(60, "1", 1);
            network.addPerson(person60);
            MyPerson person61 = new MyPerson(61, "1", 1);
            network.addPerson(person61);
            MyPerson person62 = new MyPerson(62, "1", 1);
            network.addPerson(person62);
            MyPerson person63 = new MyPerson(63, "1", 1);
            network.addPerson(person63);
            MyPerson person64 = new MyPerson(64, "1", 1);
            network.addPerson(person64);
            MyPerson person65 = new MyPerson(65, "1", 1);
            network.addPerson(person65);
            MyPerson person66 = new MyPerson(66, "1", 1);
            network.addPerson(person66);
            MyPerson person67 = new MyPerson(67, "1", 1);
            network.addPerson(person67);
            MyPerson person68 = new MyPerson(68, "1", 1);
            network.addPerson(person68);
            MyPerson person69 = new MyPerson(69, "1", 1);
            network.addPerson(person69);
            MyPerson person70 = new MyPerson(70, "1", 1);
            network.addPerson(person70);
            MyPerson person71 = new MyPerson(71, "1", 1);
            network.addPerson(person71);
            MyPerson person72 = new MyPerson(72, "1", 1);
            network.addPerson(person72);
            MyPerson person73 = new MyPerson(73, "1", 1);
            network.addPerson(person73);
            MyPerson person74 = new MyPerson(74, "1", 1);
            network.addPerson(person74);
            MyPerson person75 = new MyPerson(75, "1", 1);
            network.addPerson(person75);
            MyPerson person76 = new MyPerson(76, "1", 1);
            network.addPerson(person76);
            MyPerson person77 = new MyPerson(77, "1", 1);
            network.addPerson(person77);
            MyPerson person78 = new MyPerson(78, "1", 1);
            network.addPerson(person78);
            MyPerson person79 = new MyPerson(79, "1", 1);
            network.addPerson(person79);
            MyPerson person80 = new MyPerson(80, "1", 1);
            network.addPerson(person80);
            MyPerson person81 = new MyPerson(81, "1", 1);
            network.addPerson(person81);
            MyPerson person82 = new MyPerson(82, "1", 1);
            network.addPerson(person82);
            MyPerson person83 = new MyPerson(83, "1", 1);
            network.addPerson(person83);
            MyPerson person84 = new MyPerson(84, "1", 1);
            network.addPerson(person84);
            MyPerson person85 = new MyPerson(85, "1", 1);
            network.addPerson(person85);
            MyPerson person86 = new MyPerson(86, "1", 1);
            network.addPerson(person86);
            MyPerson person87 = new MyPerson(87, "1", 1);
            network.addPerson(person87);
            MyPerson person88 = new MyPerson(88, "1", 1);
            network.addPerson(person88);
            MyPerson person89 = new MyPerson(89, "1", 1);
            network.addPerson(person89);
            MyPerson person90 = new MyPerson(90, "1", 1);
            network.addPerson(person90);
            MyPerson person91 = new MyPerson(91, "1", 1);
            network.addPerson(person91);
            MyPerson person92 = new MyPerson(92, "1", 1);
            network.addPerson(person92);
            MyPerson person93 = new MyPerson(93, "1", 1);
            network.addPerson(person93);
            MyPerson person94 = new MyPerson(94, "1", 1);
            network.addPerson(person94);
            MyPerson person95 = new MyPerson(95, "1", 1);
            network.addPerson(person95);
            MyPerson person96 = new MyPerson(96, "1", 1);
            network.addPerson(person96);
            MyPerson person97 = new MyPerson(97, "1", 1);
            network.addPerson(person97);
            MyPerson person98 = new MyPerson(98, "1", 1);
            network.addPerson(person98);
            MyPerson person99 = new MyPerson(99, "1", 1);
            network.addPerson(person99);
            MyPerson person100 = new MyPerson(100, "1", 1);
            network.addPerson(person100);

        }

        {
            network.addRelation(1, 2, 1);
            network.addRelation(2, 3, 1);
            network.addRelation(3, 4, 1);
            network.addRelation(4, 5, 1);
            network.addRelation(5, 6, 1);
            network.addRelation(6, 7, 1);
            network.addRelation(7, 8, 1);
            network.addRelation(8, 9, 1);
            network.addRelation(9, 10, 1);
            network.addRelation(10, 11, 1);
            network.addRelation(11, 12, 1);
            network.addRelation(12, 13, 1);
            network.addRelation(13, 14, 1);
            network.addRelation(14, 15, 1);
            network.addRelation(15, 16, 1);
            network.addRelation(16, 17, 1);
            network.addRelation(17, 18, 1);
            network.addRelation(18, 19, 1);
            network.addRelation(19, 20, 1);
            network.addRelation(20, 21, 1);
            network.addRelation(21, 22, 1);
            network.addRelation(22, 23, 1);
            network.addRelation(23, 24, 1);
            network.addRelation(24, 25, 1);
            network.addRelation(25, 26, 1);
            network.addRelation(26, 27, 1);
            network.addRelation(27, 28, 1);
            network.addRelation(28, 29, 1);
            network.addRelation(29, 30, 1);
            network.addRelation(30, 31, 1);
            network.addRelation(31, 32, 1);
            network.addRelation(32, 33, 1);
            network.addRelation(33, 34, 1);
            network.addRelation(34, 35, 1);
            network.addRelation(35, 36, 1);
            network.addRelation(36, 37, 1);
            network.addRelation(37, 38, 1);
            network.addRelation(38, 39, 1);
            network.addRelation(39, 40, 1);
            network.addRelation(40, 41, 1);
            network.addRelation(41, 42, 1);
            network.addRelation(42, 43, 1);
            network.addRelation(43, 44, 1);
            network.addRelation(44, 45, 1);
            network.addRelation(45, 46, 1);
            network.addRelation(46, 47, 1);
            network.addRelation(47, 48, 1);
            network.addRelation(48, 49, 1);
            network.addRelation(49, 50, 1);
            network.addRelation(50, 51, 1);
            network.addRelation(51, 52, 1);
            network.addRelation(52, 53, 1);
            network.addRelation(53, 54, 1);
            network.addRelation(54, 55, 1);
            network.addRelation(55, 56, 1);
            network.addRelation(56, 57, 1);
            network.addRelation(57, 58, 1);
            network.addRelation(58, 59, 1);
            network.addRelation(59, 60, 1);
            network.addRelation(60, 61, 1);
            network.addRelation(61, 62, 1);
            network.addRelation(62, 63, 1);
            network.addRelation(63, 64, 1);
            network.addRelation(64, 65, 1);
            network.addRelation(65, 66, 1);
            network.addRelation(66, 67, 1);
            network.addRelation(67, 68, 1);
            network.addRelation(68, 69, 1);
            network.addRelation(69, 70, 1);
            network.addRelation(70, 71, 1);
            network.addRelation(71, 72, 1);
            network.addRelation(72, 73, 1);
            network.addRelation(73, 74, 1);
            network.addRelation(74, 75, 1);
            network.addRelation(75, 76, 1);
            network.addRelation(76, 77, 1);
            network.addRelation(77, 78, 1);
            network.addRelation(78, 79, 1);
            network.addRelation(79, 80, 1);
            network.addRelation(80, 81, 1);
            network.addRelation(81, 82, 1);
            network.addRelation(82, 83, 1);
            network.addRelation(83, 84, 1);
            network.addRelation(84, 85, 1);
            network.addRelation(85, 86, 1);
            network.addRelation(86, 87, 1);
            network.addRelation(87, 88, 1);
            network.addRelation(88, 89, 1);
            network.addRelation(89, 90, 1);
            network.addRelation(90, 91, 1);
            network.addRelation(91, 92, 1);
            network.addRelation(92, 93, 1);
            network.addRelation(93, 94, 1);
            network.addRelation(94, 95, 1);
            network.addRelation(95, 96, 1);
            network.addRelation(96, 97, 1);
            network.addRelation(97, 98, 1);
            network.addRelation(98, 99, 1);
            network.addRelation(99, 100, 1);
        }

        for (int i = 1; i <= 100; i++) {
            for (int j = 1; j <= 100; j++) {
                assertTrue(network.isCircle(i, j));
            }
        }
    }

    @Test
    public void queryBlockSum() throws EqualPersonIdException, PersonIdNotFoundException,
        EqualRelationException, RelationNotFoundException {
        MyNetwork network = new MyNetwork();

        MyPerson person100 = new MyPerson(100, "1", 1);
        MyPerson person200 = new MyPerson(200, "1", 1);
        MyPerson person300 = new MyPerson(300, "1", 1);
        MyPerson person400 = new MyPerson(400, "1", 1);

        network.addPerson(person100);
        network.addPerson(person200);
        network.addPerson(person300);
        network.addPerson(person400);

        network.addRelation(100, 200, 1);
        network.addRelation(200, 300, 1);
        network.addRelation(300, 400, 1);
        network.addRelation(400, 100, 1);

        assertEquals(network.queryBlockSum(), 1);

        network.modifyRelation(100, 400, -2);

        assertEquals(network.queryBlockSum(), 1);

        network.modifyRelation(200, 300, -2);

        assertEquals(network.queryBlockSum(), 2);

        assertFalse(network.isCircle(100, 300));
        assertFalse(network.isCircle(100, 400));

        assertFalse(network.isCircle(200, 300));
        assertFalse(network.isCircle(200, 400));

        assertTrue(network.isCircle(100, 200));
        assertTrue(network.isCircle(300, 400));
    }

    @Test
    public void testQueryTripleSum()
        throws EqualPersonIdException, PersonIdNotFoundException, EqualRelationException,
        RelationNotFoundException {
        MyNetwork network = new MyNetwork();
        network.addPerson(new MyPerson(1, "1", 1));
        network.addPerson(new MyPerson(2, "1", 1));
        network.addPerson(new MyPerson(3, "1", 1));
        network.addPerson(new MyPerson(4, "1", 1));
        network.addPerson(new MyPerson(5, "1", 1));

        Person[] oldList = network.getPersons();
        assertEquals(network.queryTripleSum(), 0);
        Person[] newList = network.getPersons();
        assertEquals(oldList.length, newList.length);
        for (int i = 0; i < oldList.length; i++) {
            MyPerson person = (MyPerson) oldList[i];
            assertTrue(person.strictEquals(newList[i]));
        }

        network.addRelation(1, 2, 1);
        network.addRelation(2, 3, 1);
        network.addRelation(3, 4, 1);
        network.addRelation(4, 1, 1);
        assertEquals(network.queryTripleSum(), 0);
        network.addRelation(1, 3, 1);
        assertEquals(network.queryTripleSum(), 2);
        network.modifyRelation(1, 2, -2);
        assertEquals(network.queryTripleSum(), 1);
        network.modifyRelation(2, 3, -2);
        network.modifyRelation(3, 4, -2);
        network.modifyRelation(4, 1, -2);
        network.modifyRelation(1, 3, -2);

        network.addRelation(1, 2, 1);
        network.addRelation(1, 3, 1);
        network.addRelation(1, 4, 1);
        network.addRelation(1, 5, 1);
        assertEquals(network.queryTripleSum(), 0);
        network.addRelation(2, 3, 1);
        network.addRelation(2, 4, 1);
        network.addRelation(2, 5, 1);
        assertEquals(network.queryTripleSum(), 3);

        network.modifyRelation(1, 2, -2);
        assertEquals(network.queryTripleSum(), 0);

        network.addRelation(4, 5, 1);
        assertEquals(network.queryTripleSum(), 2);

        network.addRelation(1, 2, 1);
        assertEquals(network.queryTripleSum(), 5);

        network.modifyRelation(1, 4, -2);
        network.modifyRelation(1, 2, -2);
        assertEquals(network.queryTripleSum(), 1);
    }
}
