import org.junit.Test;

import static org.junit.Assert.*;

public class AdventurerTest {

    @Test
    public void addBottle() {
        Adventurer adventurer = new Adventurer("alex", 123);
        adventurer.AddBottle(1, "bottle1", 1);
        assert (adventurer.GetBottleCount() == 1);
        assert (adventurer.CheckBottle(1, "bottle1"));
    }

    @Test
    public void deleteBottle() {
        Adventurer adventurer = new Adventurer("alex", 123);
        adventurer.DeleteBottle(1);
        assert (adventurer.GetBottleCount() == 0);
        assert (adventurer.CheckBottle(1, "bottle1") == false);
    }

    @Test
    public void addEquipment() {
        Adventurer adventurer = new Adventurer("alex", 123);
        adventurer.AddEquipment(2, "thePinkKnife", 5);
        assert (adventurer.GetEquipmentCount() == 1);
        assert (adventurer.CheckEquipment(2, "thePinkKnife") == true);
    }

    @Test
    public void deleteEquipment() {
        Adventurer adventurer = new Adventurer("alex", 123);
        adventurer.AddEquipment(2, "the pink knife", 5);
        assert (adventurer.GetEquipmentCount() == 0);
        assert (adventurer.CheckEquipment(1, "equip1") == false);
    }

    @Test
    public void addStar() {
    }
}