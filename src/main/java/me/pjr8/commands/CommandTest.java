package me.pjr8.commands;

import me.pjr8.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player) commandSender;
        player.sendMessage(Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).getPickaxeData().getPickaxeType() + "");
        return false;
    }
}
