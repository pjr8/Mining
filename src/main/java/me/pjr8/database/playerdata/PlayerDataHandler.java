package me.pjr8.database.playerdata;

import lombok.Getter;
import me.pjr8.Main;
import me.pjr8.mining.enums.PickaxeType;
import me.pjr8.mining.objects.PickaxeData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.*;
import java.sql.SQLException;
import java.util.Base64;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) throws SQLException, IOException {
        UUID uuid = event.getPlayer().getUniqueId();
        if (playerDao.playerExists(uuid)) {
            PlayerData playerData = playerDao.getPlayerData(uuid);
            playerData.setPickaxeData(unserializePickaxeData(playerData.getPickaxeDataSerialized()));
            playerDataHolder.put(uuid, playerData);
        } else {
            PlayerData playerData = new PlayerData();
            playerData.setUuid(uuid);
            playerData.setName(event.getPlayer().getName());
            playerData.setPickaxeData(new PickaxeData(PickaxeType.BEGINNER_PICKAXE));
            playerDataHolder.put(uuid, playerData);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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
            playerData.setPickaxeDataSerialized(serializePickaxeData(playerData.getPickaxeData()));
            playerDao.addUpdatePlayerData(playerData);
            playerDataHolder.remove(uuid);
        } catch (SQLException | IOException event) {
            Main.logger.log(Level.SEVERE, "[Mining] COULD NOT UPDATE PLAYER " + uuid + " TO DATABASE.");
        }
    }

    private String serializePickaxeData(PickaxeData pickaxeData) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(pickaxeData);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    private PickaxeData unserializePickaxeData(String toDecode) throws IOException {
        try {
            byte[] data = Base64.getDecoder().decode(toDecode);
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            PickaxeData pickaxeData = (PickaxeData) objectInputStream.readObject();
            objectInputStream.close();
            return pickaxeData;
        } catch (IOException | ClassNotFoundException event) {
            Main.logger.log(Level.SEVERE, "COULD NOT LOAD PICKAXE DATA");
            return null;
        }
    }
}
