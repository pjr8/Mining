package me.pjr8.database.spawner;

import me.pjr8.database.Database;
import me.pjr8.mob.objects.MobType;
import me.pjr8.mob.objects.SpawnerData;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;

public class SpawnerDataHandler {

    private final Database database;

    public SpawnerDataHandler(Database database) {
        this.database = database;
    }

    public HashSet<SpawnerData> loadSpawnerData() {
        HashSet<SpawnerData> toReturn = new HashSet<>();
        database.getSpawnerCollection().find().forEach(document -> {
            MobType mobType = MobType.valueOf(document.getString("spawner_type"));
            Location location = new Location(Bukkit.getWorld("flatroom"), document.getInteger("x"), document.getInteger("y"), document.getInteger("z"));
            SpawnerData spawnerData = new SpawnerData(location, mobType);
            spawnerData.setObjectId(document.getObjectId("_id"));
            toReturn.add(spawnerData);
        });
        return toReturn;
    }

    public void deleteSpawner(SpawnerData spawnerData) {
        if (spawnerData.getObjectId() != null) {
            database.getSpawnerCollection().deleteOne(new Document("_id", spawnerData.getObjectId()));
        }
    }

    public void saveSpawnersData(HashSet<SpawnerData> spawnerData) {
        spawnerData.forEach(this::saveSpawnerData);
    }

    public void saveSpawnerData(SpawnerData spawnerData) {
        if (spawnerData.getObjectId() == null) {
            Document document = new Document("spawner_type", spawnerData.getMobType())
                    .append("x", spawnerData.getSpawnerLocation().getBlockX())
                    .append("y", spawnerData.getSpawnerLocation().getBlockY())
                    .append("z", spawnerData.getSpawnerLocation().getBlockZ());
            database.getSpawnerCollection().insertOne(document);
        } else {
            Document document = new Document("spawner_type", spawnerData.getMobType().name())
                    .append("x", spawnerData.getSpawnerLocation().getBlockX())
                    .append("y", spawnerData.getSpawnerLocation().getBlockY())
                    .append("z", spawnerData.getSpawnerLocation().getBlockZ());
            database.getSpawnerCollection().replaceOne(new Document("_id", spawnerData.getObjectId()), document);
        }
    }
}
