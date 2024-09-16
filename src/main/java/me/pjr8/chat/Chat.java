package me.pjr8.chat;

import me.pjr8.database.player.PlayerData;
import me.pjr8.database.player.PlayerDataHandler;
import me.pjr8.rank.GameRank;
import me.pjr8.rank.ServerRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;

public class Chat implements Listener {

    private final PlayerDataHandler playerDataHandler;

    public Chat(PlayerDataHandler playerDataHandler) {
        this.playerDataHandler = playerDataHandler;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        PlayerData playerData = playerDataHandler.getPlayerDataHolder().get(player.getUniqueId());
        GameRank gameRank = playerData.getGameRank();
        ServerRank serverRank = playerData.getServerRank();
        String chatSend = "";
        if (serverRank == ServerRank.OWNER || serverRank == ServerRank.ADMIN) {
            chatSend = ChatColor.GOLD + "⛏ " + serverRank.getName() + ChatColor.RESET + ChatColor.GOLD + " " + ChatColor.RESET + player.getName() + ": ";
        } else {
            chatSend += ChatColor.LIGHT_PURPLE + GameRank.getLayerSymbol(gameRank.getLayer()) + ChatColor.RESET + " ";
            if (gameRank != GameRank.BEGINNER) {
                chatSend += ChatColor.RESET + "⌞" + gameRank.getName() + "⌝ ";
            }
            chatSend += player.getName();
            if (serverRank != ServerRank.USER) {
                chatSend += ChatColor.RESET + " (" + serverRank.getName() + ChatColor.RESET + "): ";
            } else {
                chatSend += ": ";
            }
        }
        chatSend += ChatColor.WHITE + event.getMessage();
        for (Player toSend : Bukkit.getOnlinePlayers()) {
            toSend.sendMessage(chatSend);
        }
    }


    public static String setGradient(String string, Color beginningColor, Color endColor) {
        StringBuilder toPrint = new StringBuilder();
        int red, green, blue;
        char[] charArray = string.toCharArray();
        int length = string.length();
        for (int i = 1 ; i < length+1 ; i++) {
            double percentage = (double) i / length;
            red = (int) Math.round(endColor.getRed() * percentage + beginningColor.getRed() * (1 - percentage));
            green = (int) Math.round(endColor.getGreen() * percentage + beginningColor.getGreen() * (1 - percentage));
            blue = (int) Math.round(endColor.getBlue() * percentage + beginningColor.getBlue() * (1 - percentage));
            toPrint.append(ChatColor.of(new Color(red, green, blue))).append(charArray[i - 1]);
        }
        return toPrint.toString();
    }
}
