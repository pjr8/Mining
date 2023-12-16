package me.pjr8.mining.enums;

import com.saicone.rtag.RtagItem;
import lombok.Getter;
import me.pjr8.Item.Item;
import me.pjr8.Item.Rarity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public enum PickaxeType {

    UNKNOWN_PICKAXE(-1, 1, Rarity.PICKAXE, Material.WOODEN_PICKAXE),
    BEGINNER_PICKAXE(1, 10, Rarity.PICKAXE, Material.WOODEN_PICKAXE),
    TEST_PICKAXE(2, 250, Rarity.PICKAXE, Material.DIAMOND_PICKAXE);

    private final int pickaxeID;
    private final int power;
    private final Rarity rarity;
    private final Material material;

    PickaxeType(int pickaxeID, int power, Rarity rarity, Material material) {
        this.pickaxeID = pickaxeID;
        this.power = power;
        this.rarity = rarity;
        this.material = material;
    }

    public static ItemStack generatePickaxe(PickaxeType pickaxeType) {
        ItemStack itemStack = new ItemStack(pickaxeType.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.RESET + "" + ChatColor.of(new Color(194, 0, 42)) + "Pickaxe Power: " + pickaxeType.getPower());
        lore.add("");
        lore.add(ChatColor.RESET + "" + ChatColor.of(pickaxeType.rarity.getColor()) + pickaxeType.rarity.getName());
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);

        RtagItem nbtItem = new RtagItem(itemStack);
        nbtItem.set(pickaxeType.getPickaxeID(), "pickaxe_id");
        itemStack = nbtItem.load();

        return itemStack;
    }

    public static PickaxeType getPickaxeTypeFromID(int pickaxeTypeID) {
        PickaxeType toReturn = UNKNOWN_PICKAXE;
        for (PickaxeType pickaxeType : PickaxeType.values()) {
            if (pickaxeType.getPickaxeID() == pickaxeTypeID) {
                toReturn = pickaxeType;
            }
        }
        return toReturn;
    }
}