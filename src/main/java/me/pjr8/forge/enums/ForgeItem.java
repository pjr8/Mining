package me.pjr8.forge.enums;

import lombok.Getter;
import me.pjr8.Item.Item;
import me.pjr8.forge.objects.ForgeItemComponent;
import me.pjr8.forge.objects.ForgeItemRequirement;
import me.pjr8.mining.enums.PickaxeType;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ForgeItem {


    //PICKAXE-RELATED FORGES
    COAL_INFUSED_PICKAXE(1, PickaxeType.COAL_INFUSED_PICKAXE, 0.5, ForgeMenuType.FORGE_MENU_PICKAXE, new ForgeItemRequirement(List.of(new ForgeItemComponent(Item.COAL, 5)))),

    //SWORD-RELATED FORGES
    COAL_SWORD(1000, Item.COAL_SWORD, 0.5, ForgeMenuType.FORGE_MENU_SWORD, new ForgeItemRequirement(List.of(new ForgeItemComponent(Item.COAL, 5), new ForgeItemComponent(Item.CHARGED_COAL, 2)))),

    //ARMOR-RELATED FORGES
    COAL_HELMET(2000, Item.COAL_HELMET, 0.5, ForgeMenuType.FORGE_MENU_ARMOR, new ForgeItemRequirement(List.of(new ForgeItemComponent(Item.COAL, 5)))),
    COAL_CHESTPLATE(2001, Item.COAL_CHESTPLATE, 0.5, ForgeMenuType.FORGE_MENU_ARMOR, new ForgeItemRequirement(List.of(new ForgeItemComponent(Item.COAL, 5)))),
    COAL_LEGGINGS(2002, Item.COAL_LEGGINGS, 0.5, ForgeMenuType.FORGE_MENU_ARMOR, new ForgeItemRequirement(List.of(new ForgeItemComponent(Item.COAL, 5)))),
    COAL_BOOTS(2003, Item.COAL_BOOTS, 0.5, ForgeMenuType.FORGE_MENU_ARMOR, new ForgeItemRequirement(List.of(new ForgeItemComponent(Item.COAL, 5)))),


    //MISC-RELATED FORGES
    COAL_MISC(3000, Item.COAL_SWORD, 0.5, ForgeMenuType.FORGE_MENU_MISC, new ForgeItemRequirement(List.of(new ForgeItemComponent(Item.COAL, 5))));


    private final int forgeItemID;
    private final Item item;
    private final double timeInMinutes;
    private final ForgeMenuType forgeMenuType;
    private final ForgeItemRequirement forgeItemRequirement;

    private final PickaxeType pickaxeType;

    ForgeItem(int forgeItemID, Item item, double timeInMinutes, ForgeMenuType forgeMenuType, ForgeItemRequirement forgeItemRequirement) {
        this.forgeItemID = forgeItemID;
        this.item = item;
        this.timeInMinutes = timeInMinutes;
        this.forgeMenuType = forgeMenuType;
        this.pickaxeType = null;
        this.forgeItemRequirement = forgeItemRequirement;
    }

    ForgeItem(int forgeItemID, PickaxeType pickaxeType, double timeInMinutes, ForgeMenuType forgeMenuType, ForgeItemRequirement forgeItemRequirement) {
        this.forgeItemID = forgeItemID;
        this.pickaxeType = pickaxeType;
        this.timeInMinutes = timeInMinutes;
        this.forgeMenuType = forgeMenuType;
        this.item = null;
        this.forgeItemRequirement = forgeItemRequirement;
    }

    public static ArrayList<ForgeItem> forgeItemByForgeType(ForgeMenuType forgeMenuType) {
        ArrayList<ForgeItem> toReturn = new ArrayList<ForgeItem>();
        for (ForgeItem forgeItem : ForgeItem.values()) {
            if (forgeItem.getForgeMenuType() == forgeMenuType) {
                toReturn.add(forgeItem);
            }
        }
        return toReturn;
    }

    public static ForgeItem getForgeItemFromItem(Item item) {
        for (ForgeItem forgeItem : ForgeItem.values()) {
            if (forgeItem.getItem() != null) {
                if (forgeItem.getItem() == item) {
                    return forgeItem;
                }
            }
        }
        return null;
    }

    public static ForgeItem getForgeItemFromPickaxeType(PickaxeType pickaxeType) {
        for (ForgeItem forgeItem : ForgeItem.values()) {
            if (forgeItem.getPickaxeType() != null) {
                if (forgeItem.getPickaxeType() == pickaxeType) {
                    return forgeItem;
                }
            }
        }
        return null;
    }

    public long getForgeTimeLong() {
        return (long) Math.round(timeInMinutes * 60000);
    }

}
