package me.pjr8.mob.commands;

import me.pjr8.Main;
import me.pjr8.mob.objects.MobType;
import me.pjr8.mob.objects.SpawnerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class CommandSpawner implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage("Usage: /spawner create <mob> | list | remove | distance <distance>");

                Main.mobHandler.spawners.forEach(spawnerData -> {
                    player.sendMessage(spawnerData.toString());
                });
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    player.sendMessage("Spawners:");
                    AtomicInteger i = new AtomicInteger();
                    Main.mobHandler.spawners.forEach(spawnerData -> player.sendMessage("[" + i.getAndIncrement() + "] " + spawnerData.getMobType().name() + " at " + spawnerData.getSpawnerLocation().toString()));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    Main.mobHandler.spawners.forEach(spawnerData -> {
                        Main.mobHandler.removeSpawner(spawnerData);
                    });

                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args[1].equalsIgnoreCase("spider")) {
                        Main.mobHandler.spawners.add(new SpawnerData(player.getLocation(), MobType.SPIDER));
                        return true;
                    } else if (args[1].equalsIgnoreCase("panda")) {
                        Main.mobHandler.spawners.add(new SpawnerData(player.getLocation(), MobType.PANDA));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("distance")) {
                    try {
                        Main.mobHandler.MOB_REMOVE_DISTANCE = Integer.parseInt(args[1]);
                        player.sendMessage("Set mob remove distance to " + Main.mobHandler.MOB_REMOVE_DISTANCE);
                        return true;
                    } catch (NumberFormatException e) {
                        player.sendMessage("Invalid number");
                    }
                }
            }
        }
        return false;
    }
}
