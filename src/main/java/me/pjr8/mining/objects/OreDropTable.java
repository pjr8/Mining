package me.pjr8.mining.objects;

import me.pjr8.Item.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class OreDropTable {

    public static ArrayList<ItemStack> getDrops(OreType oreType) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        switch(oreType) {
            case COAL_ORE:
                drops.add(Item.generateItem(Item.COAL, 1));
                break;
            case COAL_DEEPSLATE_ORE:
                drops.add(Item.generateItem(Item.COAL, 2));
                break;
            case COAL_BLOCK:
                drops.add(Item.generateItem(Item.COAL, 3));
                break;
            case COAL_BLOCK_HEAD:
                drops.add(Item.generateItem(Item.COAL, 4));
                drops.add(Item.generateItem(Item.COAL_HEAD, 2));
                break;
            case IRON_ORE:
                drops.add(Item.generateItem(Item.RAW_IRON, 1));
                break;
            case IRON_DEEPSLATE_ORE:
                drops.add(Item.generateItem(Item.RAW_IRON, 2));
                break;
            case IRON_BLOCK:
                drops.add(Item.generateItem(Item.RAW_IRON, 3));
                break;
            case IRON_BLOCK_HEAD:
                drops.add(Item.generateItem(Item.RAW_IRON, 4));
                break;
        }
        return drops;
    }


}
