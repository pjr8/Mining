package me.pjr8.mining.objects;

import me.pjr8.Item.Item;
import me.pjr8.mining.enums.OreType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class OreDropTable {

    public static ArrayList<ItemStack> getDrops(OreType oreType) {
        Random random = new Random();
        int roll = random.nextInt(100) + 1;
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        switch(oreType) {
            case COAL_ORE:
                drops.add(Item.generateItem(Item.COAL, 1));
                break;
            case COAL_DEEPSLATE_ORE:
                drops.add(Item.generateItem(Item.COAL, 2));
                break;
            case COAL_BLOCK:
                drops.add(Item.generateItem(Item.COAL, 4));
                break;
            case COAL_GILDED_BLACKSTONE:
                drops.add(Item.generateItem(Item.CHARGED_COAL, 1));
                if (roll <= 2) {
                    drops.add(Item.generateItem(Item.ENERGY_INFUSED_ORB, 1));
                }
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
