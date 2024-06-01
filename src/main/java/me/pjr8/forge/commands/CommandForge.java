package me.pjr8.forge.commands;

import me.pjr8.Main;
import me.pjr8.database.PlayerData;
import me.pjr8.forge.Forge;
import me.pjr8.forge.enums.ForgeMenuType;
import me.pjr8.forge.gui.ForgeMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandForge implements CommandExecutor {

    private final Forge forge;

    public CommandForge(Forge forge) {
        this.forge = forge;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        PlayerData playerData = Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId());
        forge.playersInForgeMenu.put(player, new ForgeMenu(player, ForgeMenuType.MAIN_MENU, 0));
        return false;
    }
}
