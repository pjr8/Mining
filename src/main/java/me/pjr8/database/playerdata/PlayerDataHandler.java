package me.pjr8.database.playerdata;

import lombok.Getter;
import me.pjr8.Main;
import me.pjr8.mining.enums.PickaxeType;
import me.pjr8.mining.objects.PickaxeData;
import me.pjr8.mining.objects.PickaxeUpgradeType;
import me.pjr8.rank.GameRank;
import me.pjr8.rank.ServerRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
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
            if (!Objects.equals(playerData.getName(), event.getPlayer().getName())) {
                Main.logger.info("Player " + playerData.getName() + " has changed name to " + event.getPlayer().getName());
                playerData.setName(event.getPlayer().getName());
            }
            playerDataHolder.put(uuid, playerData);
            if (playerData.getServerRank().equals(ServerRank.OWNER) || playerData.getServerRank().equals(ServerRank.ADMIN)) {
                Main.adminMode.add(event.getPlayer());
                event.getPlayer().setGameMode(GameMode.CREATIVE);
            }
        } else {
            PlayerData playerData = new PlayerData();
            playerData.setUuid(uuid);
            playerData.setGameRank(GameRank.BEGINNER);
            playerData.setServerRank(ServerRank.USER);
            playerData.setName(event.getPlayer().getName());
            PickaxeData pickaxeData = new PickaxeData(PickaxeType.BEGINNER_PICKAXE);
            pickaxeData.setPickaxeUpgradeTypeArrayList(new ArrayList<PickaxeUpgradeType>());
            playerData.setPickaxeData(pickaxeData);
            playerData.setPickaxeDataSerialized(serializePickaxeData(playerData.getPickaxeData()));
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

    public void updateOfflinePlayer(PlayerData playerData) {
        try {
            playerDao.addUpdatePlayerData(playerData);
        } catch (SQLException event) {
            Main.logger.log(Level.SEVERE, "[Mining] COULD NOT UPDATE PLAYER " + playerData.getName() + " TO DATABASE.");
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
