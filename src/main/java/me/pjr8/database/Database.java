package me.pjr8.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.pjr8.Main;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;


public class Database {

    public final MongoClient mongoClient;
    private static final String databaseURL = "mongodb://localhost:27017";
    public static String databaseName;

    public Database() {
        if (Bukkit.getServer().getMotd().contains("dev")) {
            databaseName = "mining_project_dev";
            Main.logger.info("Using dev mongodb collection!");
        } else {
            databaseName = "mining_project";
        }
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(new ConnectionString(databaseURL))
                .build();
        mongoClient = MongoClients.create(settings);
    }
}
