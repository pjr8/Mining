package me.pjr8.rank.commands;

import me.pjr8.Main;
import me.pjr8.database.PlayerData;
import me.pjr8.rank.GameRank;
import me.pjr8.rank.ServerRank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command line, String s, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            ServerRank serverRank = Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).getServerRank();
            if (serverRank != ServerRank.OWNER) {
                sender.sendMessage("NO PERMISSION!");
                return false;
            }
        }

        if (args.length == 0) {
            sender.sendMessage("Syntax: /rank <game || server> <rank> <optional:player>");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("game")) {
                for (GameRank gameRank : GameRank.values()) {
                    sender.sendMessage("ID: " + gameRank.toString() + ", Name: " + gameRank.getName() + ", Layer: " + gameRank.getLayer());
                }
            } else if (args[0].equalsIgnoreCase("server")) {
                for (ServerRank serverRank : ServerRank.values()) {
                    sender.sendMessage("ID: " + serverRank.toString() + ", Name: " + serverRank.getName() + ", Authority: " + serverRank.getAuthority());
                }
            } else {
                sender.sendMessage("Invalid syntax, use either 'game' or 'server'");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("game")) {
                GameRank gameRank = null;
                try {
                    gameRank = GameRank.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Invalid game rank.");
                    return false;
                }
                if (player != null) {
                    Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).setGameRank(gameRank);
                    sender.sendMessage("Successfully changed rank to " + gameRank.getName());
                } else {
                    sender.sendMessage("Console cannot change rank.");
                }
            } else if (args[0].equalsIgnoreCase("server")) {
                ServerRank serverRank = null;
                try {
                    serverRank = ServerRank.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Invalid server rank.");
                    return false;
                }
                if (player != null) {
                    Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).setServerRank(serverRank);
                    sender.sendMessage("Successfully changed rank to " + serverRank.getName());
                } else {
                    sender.sendMessage("Console cannot change rank.");
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("game")) {
                GameRank gameRank = null;
                try {
                    gameRank = GameRank.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Invalid game rank.");
                    return false;
                }
                Player playerRankChange = Bukkit.getPlayer(args[2]);
                if (playerRankChange != null) {
                    Main.playerDataHandler.getPlayerDataHolder().get(playerRankChange.getUniqueId()).setGameRank(gameRank);
                    sender.sendMessage(args[2] + " has been set to the rank " + gameRank.toString());
                } else {
                    try {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                        PlayerData playerData = Main.playerDataHandler.getPlayerData(offlinePlayer.getUniqueId());
                        playerData.setGameRank(gameRank);
                        Main.playerDataHandler.updateOfflinePlayer(playerData);
                        sender.sendMessage(args[2] + " has been set to the rank " + gameRank.toString());
                    } catch (Exception e) {
                        sender.sendMessage("Invalid username or player has never joined the server.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("server")) {
                ServerRank serverRank = null;
                try {
                    serverRank = ServerRank.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Invalid server rank.");
                    return false;
                }
                Player playerRankChange = Bukkit.getPlayer(args[2]);
                if (playerRankChange != null) {
                    Main.playerDataHandler.getPlayerDataHolder().get(playerRankChange.getUniqueId()).setServerRank(serverRank);
                    sender.sendMessage(args[2] + " has been set to the rank " + serverRank.toString());
                } else {
                    try {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                        PlayerData playerData = Main.playerDataHandler.getPlayerData(offlinePlayer.getUniqueId());
                        playerData.setServerRank(serverRank);
                        Main.playerDataHandler.updateOfflinePlayer(playerData);
                        sender.sendMessage(args[2] + " has been set to the rank " + serverRank.toString());
                    } catch (Exception e) {
                        sender.sendMessage("Invalid username or player has never joined the server.");
                    }
                }
            }
        }
        return false;
    }
}
