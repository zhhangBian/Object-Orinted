package config;

import constants.AbstractBaseTypeEnum;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributeMap {

    public static final Map<String, AbstractBaseTypeEnum> ATTRIBUTE_MAP =
            Collections.unmodifiableMap(new HashMap<String,  AbstractBaseTypeEnum>() {
                {
                    put("Food", AbstractBaseTypeEnum.FOOD);
                    put("EpicEquipment", AbstractBaseTypeEnum.EQUIPMENT);
                    put("CritEquipment", AbstractBaseTypeEnum.EQUIPMENT);
                    put("RegularEquipment", AbstractBaseTypeEnum.EQUIPMENT);
                    put("RecoverBottle", AbstractBaseTypeEnum.BOTTLE);
                    put("RegularBottle", AbstractBaseTypeEnum.BOTTLE);
                    put("ReinforcedBottle", AbstractBaseTypeEnum.BOTTLE);
                }
            });
}
