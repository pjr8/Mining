package me.pjr8.mob.mobs;

import me.pjr8.mob.objects.AbstractMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Panda;

import java.util.Objects;

public class EnemyPanda extends AbstractMob {

    public EnemyPanda() {
        super(Panda.class, 20, 5, "Panda");
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
}
