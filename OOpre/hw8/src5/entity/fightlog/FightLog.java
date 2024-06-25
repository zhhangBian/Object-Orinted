package entity.fightlog;

import config.GlobalConfig;
import constants.FightLogTypeEnum;

public class FightLog {

    private String date;

    private FightLogTypeEnum type;

    private int positiveId;

    private int negativeId;

    private String bottleName;

    private String equipmentName;

    private String adventurerName;

    private boolean isCompleteAoe;

    public FightLog(String date, FightLogTypeEnum type,
                    boolean isCompleteAoe, String adventurerName,
                    int positiveId, int negativeId,
                    String bottleName, String equipmentName) {
        this.date = date;
        this.type = type;
        this.bottleName = bottleName;
        this.positiveId = positiveId;
        this.negativeId = negativeId;
        this.adventurerName = adventurerName;
        this.equipmentName = equipmentName;
        this.isCompleteAoe = isCompleteAoe;
    }

    public String getDate() {
        return date;
    }

    public FightLogTypeEnum getType() {
        return type;
    }

    public int getPositiveId() {
        return positiveId;
    }

    public int getNegativeId() {
        return negativeId;
    }

    public boolean getIsCompleteAoe() {
        return isCompleteAoe;
    }

    @Override
    public String toString() {
        if (this.type == FightLogTypeEnum.POTION) {
            return date + " " + adventurerName + " used " + bottleName;
        } else if (this.type == FightLogTypeEnum.SINGLE_ATTACK) {
            String posName = GlobalConfig.ADVENTURERS.getNameById(positiveId);
            String negName = GlobalConfig.ADVENTURERS.getNameById(negativeId);
            return date + " " + posName + " attacked " + negName + " with " + equipmentName;
        } else {
            String posName = GlobalConfig.ADVENTURERS.getNameById(positiveId);
            return date + " " + posName + " AOE-attacked with " + equipmentName;
        }
    }
}
