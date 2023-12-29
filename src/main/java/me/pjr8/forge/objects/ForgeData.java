package me.pjr8.forge.objects;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class ForgeData implements Serializable {

    private int forgeUnlockedSlots;
    private ArrayList<ForgeSlot> forgeSlotsCurrentlyUsed;
    private ArrayList<ForgeUpgradeType> forgeUpgradeTypeList;

    public void calculateForgeSlots() {
        this.forgeUnlockedSlots = 5;
    }
}