package me.pjr8.mob.mobs;

import me.pjr8.mob.objects.IMob;
import me.pjr8.mob.objects.MobStats;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;

import java.util.Objects;

public class EnemyPanda extends MobStats implements IMob {

    public EnemyPanda() {
        setEntityType(Panda.class);
        maxHealth = 50;
        currentHealth = maxHealth;
        damage = 5;
        name = "Panda";
    }

    @Override
    public Entity spawn(Location location) {
        spawnLocation = location;
        entity = Objects.requireNonNull(spawnLocation.getWorld()).spawn(spawnLocation, getEntityType());
        Panda babyPanda = (Panda) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.PANDA);
        babyPanda.setBaby();
        babyPanda.setAgeLock(true);
        babyPanda.setCustomNameVisible(true);
        entity.addPassenger(babyPanda);
        entity.setCustomNameVisible(true);
        setCustomName();
        return entity;
    }

    @Override
    public void onAttack(Player player) {
        player.sendMessage("You have been attacked by a " + name + "!");
    }
}
