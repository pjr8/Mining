package me.pjr8;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.pjr8.Item.commands.CommandItem;
import me.pjr8.chat.Chat;
import me.pjr8.commands.CommandAdmin;
import me.pjr8.commands.CommandTest;
import me.pjr8.database.Database;
import me.pjr8.database.playerdata.PlayerDao;
import me.pjr8.database.playerdata.PlayerDataHandler;
import me.pjr8.forge.Forge;
import me.pjr8.forge.commands.CommandForge;
import me.pjr8.mining.Mining;
import me.pjr8.mining.commands.CommandPickaxeUpgrade;
import me.pjr8.rank.commands.CommandRank;
import me.pjr8.update.UpdateService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static JavaPlugin plugin;
    public static Logger logger;
    public static Database database;
    public static PlayerDao playerDao;
    public static PlayerDataHandler playerDataHandler;
    public static Mining mining;
    public static Chat chat;
    public static Forge forge;
    public static ProtocolManager protocolManager;
    public static UpdateService updateService;


    public static ArrayList<Player> adminMode = new ArrayList<Player>();


    public void onEnable() {
        plugin = this;
        logger = plugin.getLogger();
        try {
            database = new Database();
            playerDao = new PlayerDao(database.getPlayerDao());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "COULD NOT CONNECT TO DATABASE.");
            Bukkit.getServer().shutdown();
        }
        updateService = new UpdateService(this);
        updateService.start();
        playerDataHandler = new PlayerDataHandler(playerDao);
        protocolManager = ProtocolLibrary.getProtocolManager();
        mining = new Mining(playerDataHandler, protocolManager, plugin);
        chat = new Chat(playerDataHandler);
        forge = new Forge(playerDataHandler);
        Bukkit.getServer().getPluginManager().registerEvents(playerDataHandler, plugin);
        Bukkit.getServer().getPluginManager().registerEvents(mining, plugin);
        Bukkit.getServer().getPluginManager().registerEvents(chat, plugin);
        Bukkit.getServer().getPluginManager().registerEvents(forge, plugin);

        this.getCommand("item").setExecutor(new CommandItem());
        this.getCommand("admin").setExecutor(new CommandAdmin());
        this.getCommand("test").setExecutor(new CommandTest());
        this.getCommand("rank").setExecutor(new CommandRank());
        this.getCommand("pickaxeupgrade").setExecutor(new CommandPickaxeUpgrade());
        this.getCommand("forge").setExecutor(new CommandForge(forge));

        logger.info("has been enabled.");
    }

    public void onDisable() {
        try {
            playerDataHandler.shutdown();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "COULD NOT SAVE PLAYER DATA TO DATABASE");
        }
        logger.info("has been disabled.");
        updateService.stop();
    }

}