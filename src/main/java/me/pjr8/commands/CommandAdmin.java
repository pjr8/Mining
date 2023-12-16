package me.pjr8.commands;

import me.pjr8.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command line, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (Main.adminMode.contains(player)) {
            Main.adminMode.remove(player);
            player.sendMessage("Admin mode disabled");
        } else {
            Main.adminMode.add(player);
            player.sendMessage("Admin mode enabled");
        }
        return false;
    }
}
