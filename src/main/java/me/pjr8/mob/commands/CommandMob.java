package me.pjr8.mob.commands;

import me.pjr8.mob.MobHandler;
import me.pjr8.mob.objects.MobType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMob implements CommandExecutor {

    MobHandler mobHandler;

    public CommandMob(MobHandler mobHandler) {
        this.mobHandler = mobHandler;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("aggro")) {
                mobHandler.aggroAll(player);
            } else if (args[0].equalsIgnoreCase("despawn")) {
                mobHandler.despawnAll();
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("spawn")) {
                if (args[1].equalsIgnoreCase("panda")) {
                    mobHandler.addMob(MobType.PANDA, player.getLocation());
                } else if (args[1].equalsIgnoreCase("spider")) {
                    mobHandler.addMob(MobType.SPIDER, player.getLocation());
                }
            }
        }
        return false;
    }
}
