package me.pjr8.Item;

import lombok.Getter;
import me.pjr8.util.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
public enum Item {

    UNKNOWN(-1, "UNKNOWN", new ItemStack(Material.DIRT), Rarity.ABUNDANT, null),

    //Test

    TEST1(5, "Coal", new ItemStack(Material.COAL), Rarity.ABUNDANT, null),
    TEST2(6, "Coal", new ItemStack(Material.COAL), Rarity.UNCOMMON, null),
    TEST3(7, "Coal", new ItemStack(Material.COAL), Rarity.RARE, null),
    TEST4(8, "Coal", new ItemStack(Material.COAL), Rarity.EXOTIC, null),
    TEST5(9, "Coal", new ItemStack(Material.COAL), Rarity.MYTHIC, null),

    //Items

    COAL(1000, "Coal", new ItemStack(Material.COAL), Rarity.ABUNDANT, null),
    COAL_HEAD(1001, "Coal Head", SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY3YjQ3ZmJkMzdjNmQ5ZDhkMjJkOTczZTcyOTBlODA4NTJlOTI2NmEwNzZmYjdhYjIxNWFmZTkxYjgxZWQ2YyJ9fX0="), Rarity.MYTHIC, null),

    RAW_IRON(1000, "Raw Iron", new ItemStack(Material.RAW_IRON), Rarity.ABUNDANT, null),

    IRON_HEAD(1023, "Iron Gem", SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFhNzg1OTE2ZDJkMTdjYTBlYTJhZDIzZDgwMjQ3YzdjNTAyMTQ0MzkwM2JiYWI3YjI0Yjc5MzRiNmEzNjFhYiJ9fX0="), Rarity.ABUNDANT, null);


    private final int ID;
    private final String name;
    private final ItemStack itemStack;
    private final Rarity rarity;
    private final List<String> lore;

    Item(int ID, String name, ItemStack itemStack, Rarity rarity, List<String> lore) {
        this.ID = ID;
        this.name = name;
        this.itemStack = itemStack;
        this.rarity = rarity;
        this.lore = lore;
    }

    public static Item getItemFromID(int itemIDNumber) {
        Item toReturn = UNKNOWN;
        for (Item itemID : Item.values()) {
            if (itemID.getID() == itemIDNumber) {
                toReturn = itemID;
            }
        }
        return toReturn;
    }

    public static ItemStack generateItem(Item item, int amount) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "" + net.md_5.bungee.api.ChatColor.of(item.getRarity().getColor()) + item.getName());
        Rarity rarity = item.getRarity();
        if (item.getLore() != null) {
            ArrayList<String> lore = new ArrayList<String>(item.getLore());
            lore.add("");
            lore.add(rarity.getColor() + rarity.getName());
            itemMeta.setLore(lore);
        } else {
            List<String> lore = List.of(ChatColor.RESET + "" + net.md_5.bungee.api.ChatColor.of(rarity.getColor()) + rarity.getName());
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);
        return itemStack;
    }
}
