package me.pjr8.mining.objects;

import lombok.Data;
import me.pjr8.mining.enums.OreType;

import java.util.Random;

@Data
public class BlockHolder {

    private OreType oreType;
    private double durabilityMined = 0;
    private long lastTimeTouched;
    private int entityID;

    public BlockHolder(OreType oreType, long lastTimeTouched) {
        this.oreType = oreType;
        this.lastTimeTouched = lastTimeTouched;
        this.entityID = new Random().nextInt(100000) + 1000;
    }

}
