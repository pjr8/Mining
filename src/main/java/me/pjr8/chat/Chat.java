package me.pjr8.chat;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setMessage(setGradient(event.getMessage(), Color.GREEN, Color.BLUE));
    }


    public static String setGradient(String string, Color beginningColor, Color endColor) {
        String toPrint = "";

        int red = 0, green = 0, blue = 0;
        char[] charArray = string.toCharArray();
        int length = string.length();
        for (int i = 1 ; i < length+1 ; i++) {
            double percentage = (double) i / length;
            red = (int) Math.round(endColor.getRed() * percentage + beginningColor.getRed() * (1 - percentage));
            green = (int) Math.round(endColor.getGreen() * percentage + beginningColor.getGreen() * (1 - percentage));
            blue = (int) Math.round(endColor.getBlue() * percentage + beginningColor.getBlue() * (1 - percentage));
            toPrint += (ChatColor.of(new Color(red, green, blue)) + "" + charArray[i - 1]);

        }
        return toPrint;
    }


}
