package me.pjr8.database.playerdata;

import lombok.Getter;
import me.pjr8.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

@Getter
public class PlayerDataHandler implements Listener {

    private final PlayerDao playerDao;

    private final HashMap<UUID, PlayerData> playerDataHolder = new HashMap<UUID, PlayerData>();

    public PlayerDataHandler(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        UUID uuid = event.getPlayer().getUniqueId();
        if (playerDao.playerExists(uuid)) {
            playerDataHolder.put(uuid, playerDao.getPlayerData(uuid));
        } else {
            PlayerData playerData = new PlayerData();
            playerData.setUuid(uuid);
            playerData.setName(event.getPlayer().getName());
            playerDataHolder.put(uuid, playerData);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) throws SQLException {
        updateOnPlayerQuit(event.getPlayer().getUniqueId());
    }

    public void shutdown() throws SQLException {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateOnPlayerQuit(player.getUniqueId());
            player.kickPlayer(ChatColor.of("#08ff35") + "Server Restarting");
        }
    }

    private void updateOnPlayerQuit(UUID uuid) {
        try {
            PlayerData playerData = playerDataHolder.get(uuid);
            playerData.setLastJoin(System.currentTimeMillis());
            playerDao.addUpdatePlayerData(playerData);
            playerDataHolder.remove(uuid);
        } catch (SQLException event) {
            Main.logger.log(Level.SEVERE, "[Mining] COULD NOT UPDATE PLAYER " + uuid + " TO DATABASE.");
        }
    }

}
