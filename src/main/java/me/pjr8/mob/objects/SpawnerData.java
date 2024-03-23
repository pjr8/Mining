package me.pjr8.mob.objects;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;

@Data
public class SpawnerData {

    private Set<Entity> currentMobs = new HashSet<>();
    private Location spawnerLocation;
    private MobType mobType;
    private int maxMobs = 1;
    private int mobsPerSpawn = 1;
    private int spawnDelay = 5;
    private long lastSpawn = 0;

    public SpawnerData(Location spawnerLocation, MobType mobType) {
        this.spawnerLocation = spawnerLocation;
        this.mobType = mobType;
    }
}
