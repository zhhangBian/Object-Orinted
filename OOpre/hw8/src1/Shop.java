import java.util.ArrayList;

public class Shop {
    private ArrayList<Bottle> bottleRecord;
    private ArrayList<Equipment> equipmentRecord;
    private ArrayList<Food> foodRecord;

    public Shop() {
        this.bottleRecord = new ArrayList<>();
        this.equipmentRecord = new ArrayList<>();
        this.foodRecord = new ArrayList<>();
    }

    //sell

    public long SellBottle(Bottle bottle) {
        this.bottleRecord.add(bottle);
        return bottle.GetCommodity();
    }

    public long SellEquipment(Equipment equipment) {
        this.equipmentRecord.add(equipment);
        return equipment.GetCommodity();
    }

    public long SellFood(Food food) {
        this.foodRecord.add(food);
        return food.GetCommodity();
    }

    //buy

    //buy-bottle
    public int GetAveCapacity() {
        long capacitySum = 0;
        for (Bottle bottle : bottleRecord) {
            capacitySum += bottle.GetCapacity();
        }
        return (int) (capacitySum / bottleRecord.size());
    }

    public long GetAvePriceBottle() {
        long commoditySum = 0;
        for (Bottle bottle : bottleRecord) {
            commoditySum += bottle.GetCommodity();
        }
        return commoditySum / bottleRecord.size();
    }

    //buy-equipment
    public int GetAveStar() {
        long starSum = 0;
        for (Equipment equipment : equipmentRecord) {
            starSum += equipment.GetStar();
        }
        return (int) (starSum / equipmentRecord.size());
    }

    public long GetAvePriceEquipment() {
        long commoditySum = 0;
        for (Equipment equipment : equipmentRecord) {
            commoditySum += equipment.GetCommodity();
        }
        return commoditySum / equipmentRecord.size();
    }

    //buy-food
    public int GetAveEnergy() {
        long energySum = 0;
        for (Food food : foodRecord) {
            energySum += food.GetEnergy();
        }
        return (int) (energySum / foodRecord.size());
    }

    public long GetAvePriceFood() {
        long commoditySum = 0;
        for (Food food : foodRecord) {
            commoditySum += food.GetCommodity();
        }
        return commoditySum / foodRecord.size();
    }
}
