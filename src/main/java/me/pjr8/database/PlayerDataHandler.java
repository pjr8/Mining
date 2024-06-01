package me.pjr8.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import me.pjr8.Main;
import me.pjr8.forge.objects.ForgeData;
import me.pjr8.mining.objects.PickaxeData;
import me.pjr8.rank.GameRank;
import me.pjr8.rank.ServerRank;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

import static me.pjr8.database.Database.databaseName;

@Getter
public class PlayerDataHandler implements Listener {

    private final Database database;

    private final HashMap<UUID, PlayerData> playerDataHolder = new HashMap<>();

    public PlayerDataHandler(Database database) {
        this.database = database;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Document document = database.mongoClient.getDatabase(databaseName).getCollection("player_data").find(Filters.eq("uuid", uuid.toString())).first();
        if (document != null) {
            PlayerData playerData = getPlayerDataFromDocument(document);
            assert playerData != null;
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
            playerDataHolder.put(uuid, createNewPlayerData(event.getPlayer()));
            Main.logger.info("New player " + event.getPlayer().getName() + " has joined the server.");
        }
    }

    private PlayerData createNewPlayerData(Player player) {
        PlayerData playerData = new PlayerData();
        playerData.setName(player.getName());
        playerData.setUuid(player.getUniqueId());
        playerData.setGameRank(GameRank.BEGINNER);
        playerData.setServerRank(ServerRank.USER);
        if (player.getName().equalsIgnoreCase("pjr8")) {
            playerData.setServerRank(ServerRank.OWNER);
        }
        playerData.setPickaxeData(new PickaxeData());
        playerData.setForgeData(new ForgeData());
        playerData.setPlayerStats(new PlayerStats(player.getUniqueId()));
        return playerData;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        updateOnPlayerQuit(event.getPlayer().getUniqueId());
    }

    public void shutdown() throws SQLException {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateOnPlayerQuit(player.getUniqueId());
            player.kickPlayer(ChatColor.of("#08ff35") + "Server Restarting");
        }
    }

    private void updateOnPlayerQuit(UUID uuid) {
        PlayerData playerData = playerDataHolder.get(uuid);
        playerData.setLastJoin(System.currentTimeMillis());
        if (!savePlayerData(playerData)) {
            Main.logger.log(Level.SEVERE, "COULD NOT UPDATE PLAYER " + playerData.getName() + " TO DATABASE.");
        }
        playerDataHolder.remove(uuid);
    }

    public void updateOfflinePlayer(PlayerData playerData) {
        if (!savePlayerData(playerData)) {
            Main.logger.log(Level.SEVERE, "COULD NOT UPDATE PLAYER " + playerData.getName() + " TO DATABASE.");
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        Document document = database.mongoClient.getDatabase(databaseName).getCollection("player_data").find(Filters.eq("uuid", uuid)).first();
        if (document == null) {
            return null;
        } else {
            return getPlayerDataFromDocument(document);
        }
    }

    public boolean savePlayerData(PlayerData playerData) {
        try {
            Document document = new Document("uuid", playerData.getUuid().toString())
                    .append("name", playerData.getName())
                    .append("last_join", System.currentTimeMillis())
                    .append("server_rank", playerData.getServerRank().toString())
                    .append("game_rank", playerData.getGameRank().toString())
                    .append("player_stats", serializeData(playerData.getPlayerStats()))
                    .append("forge_data", serializeData(playerData.getForgeData()))
                    .append("pickaxe_data", serializeData(playerData.getPickaxeData()));
            database.mongoClient.getDatabase(databaseName).getCollection("player_data").updateOne(Filters.eq("uuid", playerData.getUuid().toString()), new Document("$set", document), new UpdateOptions().upsert(true));
            return true;
        } catch (Exception e) {
            Main.logger.log(Level.SEVERE, "COULD NOT SAVE PLAYER DATA");
            return false;
        }
    }

    private PlayerData getPlayerDataFromDocument(Document document) {
        try {
            PlayerData playerData = new PlayerData();
            playerData.setUuid(UUID.fromString(document.getString("uuid")));
            playerData.setName(document.getString("name"));
            playerData.setLastJoin(document.getLong("last_join"));
            playerData.setServerRank(ServerRank.valueOf(document.getString("server_rank")));
            playerData.setGameRank((document.getString("game_rank") == null) ? null : GameRank.valueOf(document.getString("game_rank")));
            playerData.setPlayerStats((PlayerStats) unserializeData(document.getString("player_stats")));
            playerData.setForgeData((ForgeData) unserializeData(document.getString("forge_data")));
            playerData.setPickaxeData((PickaxeData) unserializeData(document.getString("pickaxe_data")));
            return playerData;
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "COULD NOT GET PLAYER DATA FROM DOCUMENT");
            return null;
        }
    }

    private String serializeData(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    private Object unserializeData(String toDecode) throws IOException {
        try {
            byte[] data = Base64.getDecoder().decode(toDecode);
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            Object toReturn = objectInputStream.readObject();
            objectInputStream.close();
            return toReturn;
        } catch (IOException | ClassNotFoundException event) {
            Main.logger.log(Level.SEVERE, "COULD NOT UNSERIALIZE DATA");
            return null;
        }
    }
}
