package me.pjr8.forge.objects;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class ForgeData implements Serializable {

    private int forgeUnlockedSlots = 5;
    private ArrayList<ForgeSlot> forgeSlotsCurrentlyUsed = new ArrayList<>();
    private ArrayList<ForgeUpgradeType> forgeUpgradeTypeList = new ArrayList<>();

    public void calculateForgeSlots() {
        this.forgeUnlockedSlots = 5;
    }
}