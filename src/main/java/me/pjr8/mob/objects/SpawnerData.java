package me.pjr8.mob.objects;

import lombok.Data;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;

@Data
public class SpawnerData {

    private Set<Entity> currentMobs = new HashSet<>();
    private Location spawnerLocation;
    private MobType mobType;
    private int maxMobs = 5;
    private int mobsPerSpawn = 2;
    private int spawnDelay = 5;
    private long lastSpawn = 0;
    private ObjectId objectId;

    public SpawnerData(Location spawnerLocation, MobType mobType) {
        this.spawnerLocation = spawnerLocation;
        this.mobType = mobType;
    }
}
