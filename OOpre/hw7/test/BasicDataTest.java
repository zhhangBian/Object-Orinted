import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class BasicDataTest {
    BasicData basicData = new BasicData();
    Adventurer adventurer = new Adventurer("oo", 123);
    Adventurer enemy = new Adventurer("co", 456);

    @Test
    public void getAdventurerId() {
        basicData.AddAdventurer(adventurer);
        assertEquals(adventurer.GetId(), adventurer.GetId());
    }

    @Test
    public void getAdventurer() {
        basicData.AddAdventurer(adventurer);
        assertEquals(basicData.GetAdventurer(adventurer.GetName()), adventurer);
    }

    @Test
    public void addRecord() {
        Bottle bottle = new Bottle(1, "bottle", 100, 100);
        adventurer.AddBottle(bottle);
        adventurer.TakeBottle(1);
        basicData.AddRecord("2023/10", 1, adventurer.GetId(), adventurer.GetName()
                , 1, "bottle");
    }

    @Test
    public void testAddRecord() {
        Equipment equipment = new Equipment(1, "equ", 10, 100);
        adventurer.AddEquipment(equipment);
        basicData.AddRecord("2023/10", 2, adventurer.GetId(), adventurer.GetName()
                , enemy.GetId(), 1, "equ");
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
        basicData.OutFightAll();
    }

    @Test
    public void getAdventurersList() {
        ArrayList<Adventurer> arrayList = basicData.GetAdvInFightList();
    }
}