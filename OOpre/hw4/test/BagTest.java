import org.junit.Test;
import static org.junit.Assert.*;
public class BagTest{
    Adventurer adventurer = new Adventurer("Alex", 10);

    @Test
    public void renewBottleMax() {
        adventurer.AddBottle(1, "b1", 20);
        adventurer.AddFood(1, "f1", 20);
        adventurer.TakeFood(1);
        adventurer.TakeBottle(1);
        adventurer.UseFood("f1");
        adventurer.UseFood("f2");
        adventurer.AddBottle(2, "b1", 40);
        adventurer.UseBottle("b1",false);
        assert (adventurer.GetLevel() == 21);
    }

    @Test
    public void addEquipment() {
        adventurer.AddEquipment(1, "e1", 1);
        adventurer.AddStar(1);
        adventurer.AddEquipment(2, "e1", 1);
        adventurer.TakeEquipment(1);
        adventurer.TakeEquipment(2);
        assert (adventurer.GetTakeEquipmentCount() == 1);
    }

    @Test
    public void deleteEquipment() {
        adventurer.AddEquipment(1, "e1", 1);
        adventurer.AddStar(1);
        adventurer.AddEquipment(2, "e2", 1);
        adventurer.TakeEquipment(1);
        adventurer.TakeEquipment(2);
        adventurer.DeleteEquipment(1);
        assert (adventurer.GetTakeEquipmentCount() == 1);
    }

    @Test
    public void containEquipment() {
        adventurer.AddEquipment(1, "e1", 1);
        adventurer.AddStar(1);
        adventurer.AddEquipment(2, "e2", 1);
        adventurer.TakeEquipment(1);
        adventurer.TakeEquipment(2);
        //assert (adventurer.bag.ContainEquipment(1));
    }

    @Test
    public void addBottle() {
        adventurer.AddBottle(1, "b1", 20);
        adventurer.TakeBottle(1);
        //assert (adventurer.bag.ContainBottle("b1"));
    }

    @Test
    public void deleteBottle() {
        adventurer.AddBottle(1, "b1", 20);
        adventurer.TakeBottle(1);
        adventurer.DeleteBottle(1);
        //assert (!adventurer.bag.ContainBottle("b1"));
    }

    @Test
    public void containBottle() {
    }

    @Test
    public void useBottle() {
        adventurer.AddBottle(1, "b1", 20);
        adventurer.TakeBottle(1);
        adventurer.UseBottle("b1",false);
        assert (adventurer.GetHitPoint() == 520);
    }

    @Test
    public void getBottleId() {
        adventurer.AddBottle(1, "b1", 20);
        adventurer.TakeBottle(1);
        //assert (adventurer.bag.GetBottleId("b1") == 1);
    }

    @Test
    public void addFood() {
        adventurer.AddFood(1, "f1", 10);
        adventurer.TakeFood(1);
        //assert (adventurer.bag.ContainFood("f1"));
    }

    @Test
    public void deleteFood() {
        adventurer.AddFood(1, "f1", 10);
        adventurer.TakeFood(1);
        adventurer.DeleteFood(1);
        //assert (!adventurer.bag.ContainFood("f1"));
    }

    @Test
    public void useFood() {
        adventurer.AddFood(1, "f1", 10);
        adventurer.TakeFood(1);
        adventurer.UseFood("f1");
        assert (adventurer.GetLevel() == 11);
    }

    @Test
    public void containFood() {
        adventurer.AddFood(1, "f1", 10);
        adventurer.TakeFood(1);
        //assert (adventurer.bag.ContainFood("f1"));
    }

    @Test
    public void getFoodId() {
        adventurer.AddFood(1, "f1", 10);
        adventurer.TakeFood(1);
        //assert (adventurer.bag.GetFoodId("f1") == 1);
    }
}