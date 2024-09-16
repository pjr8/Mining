package me.pjr8.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import me.pjr8.Main;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;


public class Database {

    private final MongoClient mongoClient;
    private static final String databaseURL = "mongodb://localhost:27017";
    private static String databaseName;

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

        if (getPlayerCollection() == null) {
            Main.logger.severe("Player collection is null! Creating a new one!");
            mongoClient.getDatabase(databaseName).createCollection("player_data");
        }
        if (getSpawnerCollection() == null) {
            Main.logger.severe("Spawner collection is null! Creating a new one!");
            mongoClient.getDatabase(databaseName).createCollection("spawner_data");
        }
    }

    public MongoCollection<Document> getPlayerCollection() {
        return mongoClient.getDatabase(databaseName).getCollection("player_data");
    }

    public MongoCollection<Document> getSpawnerCollection() {
        return mongoClient.getDatabase(databaseName).getCollection("spawner_data");
    }

    public void shutdown() {
        mongoClient.close();
    }
}
