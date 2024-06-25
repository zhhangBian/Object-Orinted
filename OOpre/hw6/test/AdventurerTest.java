import org.junit.Test;

import static org.junit.Assert.*;

public class AdventurerTest {
    Adventurer adventurer = new Adventurer("alex", 1);

    @Test
    public void getLevel() {
        assert (adventurer.GetLevel() == 1);
    }

    @Test
    public void getHitPoint() {
        assert (adventurer.GetHitPoint() == 500);
    }

    @Test
    public void addBottle() {
        Bottle bottle = new Bottle(1, "bottle", 10, 100);
        adventurer.AddBottle(bottle);
        assert (adventurer.GetBottleCount() == 1);
    }

    @Test
    public void deleteBottle() {
        Bottle bottle = new Bottle(1, "bottle", 10, 100);
        adventurer.AddBottle(bottle);
        adventurer.DeleteBottle(1);
        assert (adventurer.GetBottleCount() == 0);
    }

    @Test
    public void checkBottle() {
        Bottle bottle = new Bottle(1, "bottle", 10, 100);
        adventurer.AddBottle(bottle);
        assert (adventurer.CheckBottle(1));
    }

    @Test
    public void useBottle() {
        Bottle bottle = new Bottle(1, "bottle", 10, 100);
        adventurer.AddBottle(bottle);
        adventurer.TakeBottle(1);
        adventurer.UseBottle("b1", false);
        adventurer.UseBottle("b1", false);
        assert (!adventurer.CheckBottle(1));
    }

    @Test
    public void addEquipment() {
        Equipment equipment = new Equipment(1, "equipment1", 10, 100);
        adventurer.AddEquipment(equipment);
        assert (adventurer.GetEquipmentCount() == 1);
    }

    @Test
    public void deleteEquipment() {
        Equipment equipment = new Equipment(1, "equipment1", 10, 100);
        adventurer.AddEquipment(equipment);
        adventurer.DeleteEquipment(1);
        assert (adventurer.GetEquipmentCount() == 0);
    }

    @Test
    public void getTakeEquipmentCount() {
        Equipment equipment = new Equipment(1, "equipment1", 10, 100);
        adventurer.AddEquipment(equipment);
        adventurer.TakeEquipment(1);
        assert (adventurer.GetTakeEquipmentCount() == 1);
    }

    @Test
    public void checkEquipment() {
        Equipment equipment = new Equipment(1, "equipment1", 10, 100);
        adventurer.AddEquipment(equipment);
        adventurer.TakeEquipment(1);
        assert (adventurer.CheckEquipment(1));
    }

    @Test
    public void addStar() {
        Equipment equipment = new Equipment(1, "equipment1", 10, 100);
        adventurer.AddEquipment(equipment);
        adventurer.AddStar(1);
        assert (adventurer.GetStar(1) == 2);
    }

    @Test
    public void addFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        assert (adventurer.GetFoodCount() == 1);
    }

    @Test
    public void deleteFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.DeleteFood(1);
        assert (adventurer.GetFoodCount() == 0);
    }

    @Test
    public void takeFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        assert (adventurer.GetFoodCount() == 1);
    }

    @Test
    public void useFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        adventurer.UseFood("f1");
        assert (adventurer.GetFoodCount() == 0);
    }
}