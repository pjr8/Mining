package me.pjr8.Item.commands;

import me.pjr8.Item.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandItem implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command line, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (args.length == 0) {
            for (Item itemID : Item.values()) {
                player.sendMessage("ID NAME: " + itemID.name() + ", ID NUMBER: " + itemID.getID());
            }
        } else if (args.length == 1) {
            player.getInventory().addItem(Item.generateItem(Item.getItemFromID(Integer.parseInt(args[0])), 1));
        } else if (args.length == 2) {
            player.getInventory().addItem(Item.IRON_HEAD.getItemStack());
        }
        return false;
    }
}
