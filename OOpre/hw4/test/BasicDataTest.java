import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class BasicDataTest {
    BasicData basicData = new BasicData();
    HashMap<Integer, Adventurer> hashMap = basicData.GetHashMap();
    Adventurer adventurer = new Adventurer("oo", 123);
    Adventurer enemy = new Adventurer("co", 456);

    @Test
    public void getHashMap() {
        HashMap<Integer, Adventurer> hashMap = basicData.GetHashMap();
        assertTrue(hashMap.isEmpty());
    }

    @Test
    public void getAdventurerId() {
        hashMap.put(adventurer.GetId(), adventurer);
        assertEquals(adventurer.GetId(), adventurer.GetId());
    }

    @Test
    public void getAdventurer() {
        hashMap.put(adventurer.GetId(),adventurer);
        assertEquals(basicData.GetAdventurer(adventurer.GetName()), adventurer);
    }

    @Test
    public void addRecord() {
        adventurer.AddBottle(1, "bottle", 100);
        adventurer.TakeBottle(1);
        basicData.AddRecord("2023/10", 1, adventurer.GetId(), adventurer.GetName()
                , 1, "bottle", basicData);
    }

    @Test
    public void testAddRecord() {
        adventurer.AddEquipment(1, "equ", 10);
        basicData.AddRecord("2023/10", 2, adventurer.GetId(), adventurer.GetName()
                , enemy.GetId(), 1, "equ", basicData);
    }

    @Test
    public void checkRecordDate() {
        basicData.CheckRecordDate("2023/10");
    }

    @Test
    public void printFight() {
        Record record = new Record("2023/10", 1, adventurer.GetId(), adventurer.GetName(),
                1, "bottle", basicData);
        basicData.PrintFight(record);
    }

    @Test
    public void checkRecordFighter() {
        basicData.CheckRecordFighter(adventurer.GetId());
    }

    @Test
    public void checkRecordFighted() {
        basicData.CheckRecordFighted(adventurer.GetId());
    }

    @Test
    public void addAdventurerInFight() {
        basicData.AddAdventurerInFight(adventurer);
    }

    @Test
    public void outFightAll() {
        basicData.OutFightAll(hashMap);
    }

    @Test
    public void getAdventurersList() {
        ArrayList<Adventurer> arrayList = basicData.GetAdventurersList();
    }
}