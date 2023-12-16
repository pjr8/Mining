package me.pjr8.mining.enums;

import lombok.Getter;

import me.pjr8.util.SkullCreator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@Getter
public enum OreType {

    //Ores

    COAL_ORE(1, 100, new ItemStack(Material.COAL_ORE)),
    COAL_DEEPSLATE_ORE(2, 150, new ItemStack(Material.DEEPSLATE_COAL_ORE)),
    COAL_BLOCK(3, 200, new ItemStack(Material.COAL_BLOCK)),
    COAL_BLOCK_HEAD(4, 250, SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY3YjQ3ZmJkMzdjNmQ5ZDhkMjJkOTczZTcyOTBlODA4NTJlOTI2NmEwNzZmYjdhYjIxNWFmZTkxYjgxZWQ2YyJ9fX0=")),

    IRON_ORE(1, 500, new ItemStack(Material.IRON_ORE)),
    IRON_DEEPSLATE_ORE(2, 1000, new ItemStack(Material.DEEPSLATE_IRON_ORE)),
    IRON_BLOCK(3, 1500, new ItemStack(Material.IRON_BLOCK)),
    IRON_BLOCK_HEAD(4, 2000, SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFhNzg1OTE2ZDJkMTdjYTBlYTJhZDIzZDgwMjQ3YzdjNTAyMTQ0MzkwM2JiYWI3YjI0Yjc5MzRiNmEzNjFhYiJ9fX0="));

    private final ItemStack itemStack;
    private final int level;
    private final int durability;

    OreType(int level, int durability, ItemStack itemStack) {
        this.level = level;
        this.durability = durability;
        this.itemStack = itemStack;
    }

    public static OreType getOreTypeFromBlock(Block block) {
        for (OreType oreType : OreType.values()) {
            if (oreType.getItemStack().getType() == block.getType()) {
                return oreType;
            }
        }
        return null;
    }
}
