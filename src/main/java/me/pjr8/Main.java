package me.pjr8;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.pjr8.Item.commands.CommandItem;
import me.pjr8.chat.Chat;
import me.pjr8.commands.CommandAdmin;
import me.pjr8.commands.CommandTest;
import me.pjr8.database.Database;
import me.pjr8.database.PlayerDataHandler;
import me.pjr8.forge.Forge;
import me.pjr8.forge.commands.CommandForge;
import me.pjr8.mining.Mining;
import me.pjr8.mining.commands.CommandPickaxeUpgrade;
import me.pjr8.mob.MobHandler;
import me.pjr8.mob.commands.CommandMob;
import me.pjr8.mob.commands.CommandSpawner;
import me.pjr8.rank.commands.CommandRank;
import me.pjr8.update.UpdateService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static JavaPlugin plugin;
    public static Logger logger;
    public static Database database;
    public static PlayerDataHandler playerDataHandler;
    public static Mining mining;
    public static Chat chat;
    public static Forge forge;
    public static ProtocolManager protocolManager;
    public static UpdateService updateService;
    public static MobHandler mobHandler;
    public static HashSet<Player> adminMode = new HashSet<Player>();

    public void onEnable() {
        registerClasses();
        registerEvents();
        registerCommands();
        logger.info("has been enabled.");
    }

    public void onDisable() {
        shutdown();
    }

    public void registerClasses() {
        plugin = this;
        logger = plugin.getLogger();
        database = new Database();
        updateService = new UpdateService(this);
        updateService.start();
        playerDataHandler = new PlayerDataHandler(database);
        protocolManager = ProtocolLibrary.getProtocolManager();
        mining = new Mining(playerDataHandler, protocolManager, plugin);
        chat = new Chat(playerDataHandler);
        forge = new Forge(playerDataHandler);
        mobHandler = new MobHandler();
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(playerDataHandler, plugin);
        getServer().getPluginManager().registerEvents(mining, plugin);
        getServer().getPluginManager().registerEvents(chat, plugin);
        getServer().getPluginManager().registerEvents(forge, plugin);
        getServer().getPluginManager().registerEvents(mobHandler, plugin);
    }

    public void registerCommands() {
        getCommand("item").setExecutor(new CommandItem());
        getCommand("admin").setExecutor(new CommandAdmin());
        getCommand("test").setExecutor(new CommandTest());
        getCommand("rank").setExecutor(new CommandRank());
        getCommand("pickaxeupgrade").setExecutor(new CommandPickaxeUpgrade());
        getCommand("forge").setExecutor(new CommandForge(forge));
        getCommand("mob").setExecutor(new CommandMob(mobHandler));
        getCommand("spawner").setExecutor(new CommandSpawner());
    }

    public void shutdown() {
        mobHandler.shutdown();
        try {
            playerDataHandler.shutdown();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "COULD NOT SAVE PLAYER DATA TO DATABASE");
        }
        logger.info("has been disabled.");
        updateService.stop();
    }
}