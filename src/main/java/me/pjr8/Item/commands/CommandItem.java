package me.pjr8.Item.commands;

import com.saicone.rtag.RtagItem;
import me.pjr8.Item.Item;
import me.pjr8.mining.enums.PickaxeType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandItem implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command line, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (args.length == 0) {
            for (Item itemID : Item.values()) {
                player.sendMessage("ID NUMBER: " + itemID.getID() + ", ID NAME: " + itemID.name());

            }
        } else if (args.length == 1) {
            player.getInventory().addItem(Item.generateItem(Item.getItemFromID(Integer.parseInt(args[0])), 1));
        } else if (args.length == 2) {
            player.getInventory().addItem(PickaxeType.generatePickaxe(PickaxeType.getPickaxeTypeFromID(Integer.parseInt(args[0]))));
        } else if (args.length == 3) {
            ItemStack itemStack = player.getItemInHand();
            RtagItem nbtItem = new RtagItem(itemStack);
            player.sendMessage("Pickaxe ID: " + nbtItem.getOptional("pickaxe_id").asInt());
            player.sendMessage("Item ID: " + nbtItem.getOptional("item_id").asInt());
        }
        return false;
    }
}
