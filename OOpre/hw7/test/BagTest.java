import org.junit.Test;

public class BagTest {
    Adventurer adventurer = new Adventurer("Alex", 10);
    Shop shop = new Shop();

    @Test
    public void renewBottleMax() {
        Bottle bottle1 = new Bottle(1, "b1", 10, 100);
        adventurer.AddBottle(bottle1);
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        adventurer.TakeBottle(1);
        adventurer.UseFood("f1");
        adventurer.UseFood("f2");
        Bottle bottle2 = new Bottle(1, "b2", 40, 100);
        adventurer.AddBottle(bottle2);
        adventurer.UseBottle("b1", false);
        assert (adventurer.GetLevel() == 21);
    }

    @Test
    public void addEquipment() {
        Equipment equipment1 = new Equipment(1, "e1", 1, 100);
        adventurer.AddEquipment(equipment1);
        adventurer.AddStar(1);
        Equipment equipment2 = new Equipment(2, "e1", 1, 100);
        adventurer.AddEquipment(equipment2);
        adventurer.TakeEquipment(1);
        adventurer.TakeEquipment(2);
        assert (adventurer.GetTakeEquipmentCount() == 1);
    }

    @Test
    public void deleteEquipment() {
        Equipment equipment1 = new Equipment(1, "e1", 1, 100);
        adventurer.AddEquipment(equipment1);
        adventurer.AddStar(1);
        Equipment equipment2 = new Equipment(1, "e1", 1, 100);
        adventurer.AddEquipment(equipment2);
        adventurer.TakeEquipment(1);
        adventurer.TakeEquipment(2);
        adventurer.SellEquipment(1, shop);
        assert (adventurer.GetTakeEquipmentCount() == 1);
    }

    @Test
    public void containEquipment() {
        Equipment equipment1 = new Equipment(1, "e1", 1, 100);
        adventurer.AddEquipment(equipment1);
        adventurer.AddStar(1);
        Equipment equipment2 = new Equipment(1, "e1", 1, 100);
        adventurer.AddEquipment(equipment2);
        adventurer.TakeEquipment(1);
        adventurer.TakeEquipment(2);
        //assert (adventurer.bag.ContainEquipment(1));
    }

    @Test
    public void addBottle() {
        Bottle bottle1 = new Bottle(1, "b1", 10, 100);
        adventurer.AddBottle(bottle1);
        adventurer.TakeBottle(1);
        //assert (adventurer.bag.ContainBottle("b1"));
    }

    @Test
    public void deleteBottle() {
        Bottle bottle1 = new Bottle(1, "b1", 10, 100);
        adventurer.AddBottle(bottle1);
        adventurer.TakeBottle(1);
        adventurer.SellBottle(1, shop);
        //assert (!adventurer.bag.ContainBottle("b1"));
    }

    @Test
    public void containBottle() {
    }

    @Test
    public void useBottle() {
        Bottle bottle1 = new Bottle(1, "b1", 10, 100);
        adventurer.AddBottle(bottle1);
        adventurer.TakeBottle(1);
        adventurer.UseBottle("b1", false);
        assert (adventurer.GetHitPoint() == 520);
    }

    @Test
    public void getBottleId() {
        Bottle bottle1 = new Bottle(1, "b1", 10, 100);
        adventurer.AddBottle(bottle1);
        adventurer.TakeBottle(1);
        //assert (adventurer.bag.GetBottleId("b1") == 1);
    }

    @Test
    public void addFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        //assert (adventurer.bag.ContainFood("f1"));
    }

    @Test
    public void deleteFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        adventurer.SellFood(1, shop);
        //assert (!adventurer.bag.ContainFood("f1"));
    }

    @Test
    public void useFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        adventurer.UseFood("f1");
        assert (adventurer.GetLevel() == 11);
    }

    @Test
    public void containFood() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        //assert (adventurer.bag.ContainFood("f1"));
    }

    @Test
    public void getFoodId() {
        Food food = new Food(1, "f1", 10, 100);
        adventurer.AddFood(food);
        adventurer.TakeFood(1);
        //assert (adventurer.bag.GetFoodId("f1") == 1);
    }
}