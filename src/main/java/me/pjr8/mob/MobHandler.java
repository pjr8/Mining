package me.pjr8.mob;

import me.pjr8.mob.mobs.EnemyPanda;
import me.pjr8.mob.mobs.EnemySpider;
import me.pjr8.mob.objects.IMob;
import me.pjr8.mob.objects.MobType;
import me.pjr8.mob.objects.SpawnerData;
import me.pjr8.update.UpdateEvent;
import me.pjr8.update.UpdateType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.HashSet;

public class MobHandler implements Listener {

    public final HashMap<Entity, IMob> currentMobs = new HashMap<>();

    public final HashSet<SpawnerData> spawners = new HashSet<>();

    public int MOB_REMOVE_DISTANCE = 25;

    @EventHandler
    public void doSpawner(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) return;
        spawners.forEach((spawnerData -> {
            spawnerData.getCurrentMobs().forEach(this::removeMobIfOutOfRange);
            if (spawnerData.getCurrentMobs().size() < spawnerData.getMaxMobs() && System.currentTimeMillis() - spawnerData.getLastSpawn() > (spawnerData.getSpawnDelay()) * 1000L) {
                for (int i = 0; i < spawnerData.getMobsPerSpawn(); i++) {
                    IMob iMob = getMobByType(spawnerData.getMobType());
                    iMob.setSpawnLocation(spawnerData.getSpawnerLocation());
                    iMob.setSpawner(spawnerData);
                    spawnerData.getCurrentMobs().add(iMob.spawn(spawnerData.getSpawnerLocation()));
                    currentMobs.put(iMob.getEntity(), iMob);
                }
                spawnerData.setLastSpawn(System.currentTimeMillis());
            }
        }));
    }

    private void removeMobIfOutOfRange(Entity entity) {
        IMob iMob = currentMobs.get(entity);
        if (entity.getLocation().distance(iMob.getSpawnLocation()) > MOB_REMOVE_DISTANCE) {
            entity.getPassengers().forEach(Entity::remove);
            currentMobs.get(entity).getSpawner().getCurrentMobs().remove(entity);
            currentMobs.remove(entity);
            entity.remove();
        }
    }

    @EventHandler
    public void onPlayerDamageMob(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof Mob) {
            event.setCancelled(true);
            if (currentMobs.containsKey(event.getEntity())) {
                currentMobs.get(event.getEntity()).onDamage((player));
                event.getEntity().setVelocity(player.getLocation().getDirection().multiply(0.75));
                return;
            }
        }
        if (event.getEntity() instanceof Panda entity) {
            if (entity.isInsideVehicle() && !entity.isAdult()) {
                if (currentMobs.containsKey(entity.getVehicle())) {
                    currentMobs.get(entity.getVehicle()).onDamage((Player) event.getDamager());
                }
            }
        }
    }

    @EventHandler
    public void onMobDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Mob && event.getEntity() instanceof Player player) {
            if (currentMobs.containsKey(event.getDamager())) {
                event.setCancelled(true);
                player.sendMessage("You have been attacked by a mob!");
            }
        }
    }

    public void addMob(MobType mobType, Location location) {
        IMob iMob = getMobByType(mobType);
        iMob.setSpawnLocation(location);
        currentMobs.put(iMob.spawn(location), iMob);
    }

    public void aggroAll(Player player) {
        for (Entity entity : currentMobs.keySet()) {
            if (entity instanceof Mob) {
                ((Mob) entity).setTarget(player);
            }
        }
    }

    public void despawnAll() {
        currentMobs.forEach((entity, iMob) -> {
            entity.getPassengers().forEach(Entity::remove);
            iMob.getSpawner().getCurrentMobs().remove(entity);
            entity.remove();
        });
        currentMobs.clear();
    }

    public void removeMob(Entity entity) {
        IMob iMob = currentMobs.get(entity);
        entity.getPassengers().forEach(Entity::remove);
        iMob.getSpawner().getCurrentMobs().remove(entity);
        entity.remove();
        currentMobs.remove(entity);
    }

    public void shutdown() {
        for (Entity entity : currentMobs.keySet()) {
            for (Entity passenger : entity.getPassengers()) {
                passenger.remove();
            }
            entity.remove();
        }
    }

    private IMob getMobByType(MobType mobType) {
        return switch (mobType) {
            case PANDA -> new EnemyPanda();
            case SPIDER -> new EnemySpider();
        };
    }
}
