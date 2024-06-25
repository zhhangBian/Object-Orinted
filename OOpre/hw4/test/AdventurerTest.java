import org.junit.Test;
import static org.junit.Assert.*;

public class AdventurerTest {
    Adventurer adventurer=new Adventurer("alex",1);
    @Test
    public void getLevel() {
        assert (adventurer.GetLevel()==1);
    }

    @Test
    public void getHitPoint() {
        assert (adventurer.GetHitPoint()==500);
    }

    @Test
    public void addBottle() {
        adventurer.AddBottle(1,"b1",10);
        assert (adventurer.GetBottleCount()==1);
    }

    @Test
    public void deleteBottle() {
        adventurer.AddBottle(1,"b1",10);
        adventurer.DeleteBottle(1);
        assert (adventurer.GetBottleCount()==0);
    }

    @Test
    public void checkBottle() {
        adventurer.AddBottle(1,"b1",10);
        assert (adventurer.CheckBottle(1));
    }

    @Test
    public void useBottle() {
        adventurer.AddBottle(1,"b1",10);
        adventurer.TakeBottle(1);
        adventurer.UseBottle("b1",false);
        adventurer.UseBottle("b1",false);
        assert (!adventurer.CheckBottle(1));
    }

    @Test
    public void addEquipment() {
        adventurer.AddEquipment(1,"e1",1);
        assert (adventurer.GetEquipmentCount()==1);
    }

    @Test
    public void deleteEquipment() {
        adventurer.AddEquipment(1,"e1",1);
        adventurer.DeleteEquipment(1);
        assert (adventurer.GetEquipmentCount()==0);
    }

    @Test
    public void getTakeEquipmentCount() {
        adventurer.AddEquipment(1,"e1",1);
        adventurer.TakeEquipment(1);
        assert (adventurer.GetTakeEquipmentCount()==1);
    }

    @Test
    public void checkEquipment() {
        adventurer.AddEquipment(1,"e1",1);
        adventurer.TakeEquipment(1);
        assert (adventurer.CheckEquipment(1));
    }

    @Test
    public void addStar() {
        adventurer.AddEquipment(1,"e1",1);
        adventurer.AddStar(1);
        assert (adventurer.GetStar(1)==2);
    }

    @Test
    public void addFood() {
        adventurer.AddFood(1,"f1",10);
        assert (adventurer.GetFoodCount()==1);
    }

    @Test
    public void deleteFood() {
        adventurer.AddFood(1,"f1",10);
        adventurer.DeleteFood(1);
        assert (adventurer.GetFoodCount()==0);
    }

    @Test
    public void takeFood() {
        adventurer.AddFood(1,"f1",10);
        adventurer.TakeFood(1);
        assert (adventurer.GetFoodCount()==1);
    }

    @Test
    public void useFood() {
        adventurer.AddFood(1,"f1",10);
        adventurer.TakeFood(1);
        adventurer.UseFood("f1");
        assert (adventurer.GetFoodCount()==0);
    }
}