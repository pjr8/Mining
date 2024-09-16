package me.pjr8.commands;

import me.pjr8.Main;
import me.pjr8.database.player.PlayerData;
import me.pjr8.mining.objects.PickaxeUpgradeType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        PlayerData playerData = Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId());
        player.sendMessage(playerData.getUuid());
        playerData.getPickaxeData().addPickaxeUpgrade(PickaxeUpgradeType.ENERGY_INFUSED_ORB_UPGRADE);
        player.sendMessage(playerData.getPickaxeData().getPickaxeUpgradeTypeArrayList().toString());





        /*player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));
        player.playSound(player, Sound.BLOCK_END_PORTAL_SPAWN, 2, 1f);
        player.sendTitle(ChatColor.of(new Color(87, 32, 94)) + "" + ChatColor.BOLD + "Mystspore Grotto", ChatColor.of(new Color(120, 83, 150)) + "Where Shadows Whisper and Magic Dwells", 10,80 ,20);*/
        return false;
    }
}
