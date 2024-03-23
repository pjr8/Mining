package me.pjr8.Item;

import com.saicone.rtag.RtagItem;
import lombok.Getter;
import me.pjr8.Main;
import me.pjr8.forge.enums.ForgeItem;
import me.pjr8.mining.enums.PickaxeType;
import me.pjr8.util.SkullCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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

    COAL_SWORD(20, "Coal Sword", makeGlow(new ItemStack(Material.STONE_SWORD)), Rarity.EXOTIC, null),

    COAL_HELMET(21, "Coal Sword", makeGlow(new ItemStack(Material.LEATHER_HELMET)), Rarity.EXOTIC, null),
    COAL_CHESTPLATE(22, "Coal Sword", makeGlow(new ItemStack(Material.LEATHER_CHESTPLATE)), Rarity.EXOTIC, null),
    COAL_LEGGINGS(23, "Coal Sword", makeGlow(new ItemStack(Material.LEATHER_LEGGINGS)), Rarity.EXOTIC, null),
    COAL_BOOTS(24, "Coal Sword", makeGlow(new ItemStack(Material.LEATHER_BOOTS)), Rarity.EXOTIC, null),


    //Items

    COAL(1_000, "Coal", new ItemStack(Material.COAL), Rarity.ABUNDANT, null),

    CHARGED_COAL(1_001, "Charged Coal", makeGlow(new ItemStack(Material.COAL)), Rarity.UNCOMMON, List.of(ChatColor.of("#DEDEDE") + "It emits a bright light...")),

    COAL_HEAD(1_005, "Coal Head", SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY3YjQ3ZmJkMzdjNmQ5ZDhkMjJkOTczZTcyOTBlODA4NTJlOTI2NmEwNzZmYjdhYjIxNWFmZTkxYjgxZWQ2YyJ9fX0="), Rarity.MYTHIC, null),

    RAW_IRON(1_050, "Raw Iron", new ItemStack(Material.RAW_IRON), Rarity.ABUNDANT, null),

    IRON_HEAD(1_051, "Iron Gem", SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFhNzg1OTE2ZDJkMTdjYTBlYTJhZDIzZDgwMjQ3YzdjNTAyMTQ0MzkwM2JiYWI3YjI0Yjc5MzRiNmEzNjFhYiJ9fX0="), Rarity.ABUNDANT, null),

    //Mob Drops

    BAMBOO(5_000, "Bamboo", new ItemStack(Material.BAMBOO), Rarity.ABUNDANT, null),
    STRING(5_001, "String", new ItemStack(Material.STRING), Rarity.ABUNDANT, null),





    //Upgrade Items

    ENERGY_INFUSED_ORB(10_000, ChatColor.of("#ff9c2b") + "Energy-Infused Orb", makeGlow(new ItemStack(Material.FIRE_CHARGE)), Rarity.UPGRADE, List.of(ChatColor.of("#DEDEDE") + "It seems to give power...")),

    SHARP_COAL_SHARD(10_002, ChatColor.of("#828282") + "Sharp Coal Shard", makeGlow(new ItemStack(Material.FLINT)), Rarity.UPGRADE, List.of(ChatColor.of("#DEDEDE") + "Maybe it could be used to sharpen something...")),


    //Task-related items

    CORRUPTED_SHARD_MYSTSPORE(20_000, ChatColor.DARK_RED + "" + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.DARK_RED + "Corr" + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.DARK_RED + "upted Sha" + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.DARK_RED + "rd" + ChatColor.MAGIC + "|", makeGlow(new ItemStack(Material.ECHO_SHARD)), Rarity.QUEST, List.of(ChatColor.of("#DEDEDE") + "It omits mysterious energy...")),
    CURSED_REMAINS_MYSTSPORE(20_001, ChatColor.RESET + "" + ChatColor.of("#ff3d3d") + "Cursed Remains", makeGlow(new ItemStack(Material.BROWN_DYE)), Rarity.QUEST, List.of(ChatColor.of("#DEDEDE") + "It still seems alive..."));

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

    public static Item getItemFromItemStack(ItemStack itemStack) {
        try {
            RtagItem rtagItem = new RtagItem(itemStack);
            Integer item_id = rtagItem.getOptional("item_id").asInt();
            return getItemFromID(item_id);
        } catch (Exception e) {
            Main.logger.info("Player had invalid item");
            return null;
        }
    }

    public static Integer getItemIDFromItemStack(ItemStack itemStack) {
        RtagItem rtagItem = new RtagItem(itemStack);
        return rtagItem.getOptional("item_id").asInt();
    }


    public ItemStack generate(int amount) {
        return generateItem(this, amount);
    }



    public static ItemStack generateItem(Item item, int amount) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (item.getRarity() == Rarity.QUEST || item.getRarity() == Rarity.MISC || item.getRarity() == Rarity.UPGRADE) {
            itemMeta.setDisplayName(item.getName());
        } else {
            itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.of(item.getRarity().getColor()) + item.getName());
        }
        Rarity rarity = item.getRarity();
        if (item.getLore() != null) {
            ArrayList<String> lore = new ArrayList<String>(item.getLore());
            lore.add("");
            lore.add(ChatColor.RESET + "" + ChatColor.of(rarity.getColor()) + rarity.getName());
            itemMeta.setLore(lore);
        } else {
            List<String> lore = List.of(ChatColor.RESET + "" + ChatColor.of(rarity.getColor()) + rarity.getName());
            itemMeta.setLore(lore);
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        if (amount > 64) {
            itemStack.setAmount(64);
        } else {
            itemStack.setAmount(amount);
        }
        RtagItem nbtItem = new RtagItem(itemStack);
        nbtItem.set(item.getID(), "item_id");
        itemStack = nbtItem.load();

        return itemStack;
    }

    private static ItemStack makeGlow(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
