package me.pjr8.mining.objects;

import lombok.Data;
import me.pjr8.mining.enums.PickaxeType;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class PickaxeData implements Serializable {

    private PickaxeType pickaxeType;
    private ArrayList<PickaxeUpgradeType> pickaxeUpgradeTypeArrayList;

    public PickaxeData(PickaxeType pickaxeType) {
        this.pickaxeType = pickaxeType;
    }

    public int calculatePickaxePower() {
        double power = pickaxeType.getPower();

        for (PickaxeUpgradeType pickaxeUpgradeType : pickaxeUpgradeTypeArrayList) {
            power += applyUpgrade(pickaxeUpgradeType, power);
        }

        return (int) Math.round(power);
    }

    private double applyUpgrade(PickaxeUpgradeType pickaxeUpgradeType, double inputPower) {
        return switch (pickaxeUpgradeType) {
            case ENERGY_INFUSED_ORB_UPGRADE -> 2;
            case TESTING -> 100000;
        };
    }

    public void addPickaxeUpgrade(PickaxeUpgradeType pickaxeUpgradeType) {
        if (!pickaxeUpgradeTypeArrayList.contains(pickaxeUpgradeType)) {
            this.pickaxeUpgradeTypeArrayList.add(pickaxeUpgradeType);
        }
    }
    public void removePickaxeUpgrade(PickaxeUpgradeType pickaxeUpgradeType) {
        if (pickaxeUpgradeTypeArrayList.contains(pickaxeUpgradeType)) {
            this.pickaxeUpgradeTypeArrayList.remove(pickaxeUpgradeType);
        }
    }
    public boolean hasPickaxeUpgrade(PickaxeUpgradeType pickaxeUpgradeType) {
        return this.pickaxeUpgradeTypeArrayList.contains(pickaxeUpgradeType);
    }
}
