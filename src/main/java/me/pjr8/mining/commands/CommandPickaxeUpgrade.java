package me.pjr8.mining.commands;

import me.pjr8.Main;
import me.pjr8.database.playerdata.PlayerData;
import me.pjr8.mining.objects.PickaxeUpgradeType;
import me.pjr8.rank.ServerRank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPickaxeUpgrade implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        if (Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).getServerRank() != ServerRank.OWNER) {
            return false;
        }
        if (args.length != 1) {
            for (PickaxeUpgradeType pickaxeUpgradeType : PickaxeUpgradeType.values()) {
                player.sendMessage("Upgrade ID: " + pickaxeUpgradeType.name());
            }
            player.sendMessage("Syntax: /pickaxeupgrade <upgrade>");
        } else {
            try {
                PickaxeUpgradeType pickaxeUpgradeType = PickaxeUpgradeType.valueOf(args[0].toUpperCase());
                PlayerData playerData = Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId());
                if (playerData.getPickaxeData().hasPickaxeUpgrade(pickaxeUpgradeType)) {
                    playerData.getPickaxeData().removePickaxeUpgrade(pickaxeUpgradeType);
                    player.sendMessage(pickaxeUpgradeType.name() + " has successfully been removed.");
                } else {
                    playerData.getPickaxeData().addPickaxeUpgrade(pickaxeUpgradeType);
                    player.sendMessage(pickaxeUpgradeType.name() + " has successfully been added.");
                }
            } catch (Exception e) {
                player.sendMessage("Invalid pickaxe upgrade type");
            }
        }
        return false;
    }
}
