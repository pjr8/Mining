package me.pjr8.mining.objects;

import lombok.Data;
import me.pjr8.mining.enums.PickaxeType;

import java.io.Serializable;

@Data
public class PickaxeData implements Serializable {

    private PickaxeType pickaxeType;

    public PickaxeData(PickaxeType pickaxeType) {
        this.pickaxeType = pickaxeType;
    }

    public int calculatePickaxePower() {
        return pickaxeType.getPower();
    }

}
