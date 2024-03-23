package me.pjr8.mob.objects;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Data
public class MobStats {

    protected Entity entity;
    protected Class<? extends Entity> entityType;
    protected SpawnerData spawner;
    protected Location spawnLocation;
    protected String name;
    protected int maxHealth;
    protected double currentHealth;
    protected double damage;
    protected int speed = 100;
    protected HashMap<Player, Double> damageDealt = new HashMap<>();
}
