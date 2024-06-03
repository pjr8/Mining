package me.pjr8.mob.objects;

import lombok.Data;
import me.pjr8.Item.Item;
import me.pjr8.Main;
import me.pjr8.mob.MobDropTable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

@Data
public abstract class AbstractMob {

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

/*    abstract void onAttack(Player player);
    abstract void setSpawnLocation(Location location);
    abstract String getName();
    abstract void setName(String name);
    abstract Location getSpawnLocation();
    abstract void setCurrentHealth(double health);
    abstract double getCurrentHealth();
    abstract void setMaxHealth(int health);
    abstract int getMaxHealth();
    abstract void setDamage(double damage);
    abstract double getDamage();
    abstract void setSpeed(int speed);
    abstract int getSpeed();
    abstract HashMap<Player, Double> getDamageDealt();
    abstract Entity getEntity();
    abstract void setEntity(Entity entity);
    abstract Class<? extends Entity> getEntityType();
    abstract SpawnerData getSpawner();
    abstract void setSpawner(SpawnerData spawner);*/

    public AbstractMob(Class<? extends Entity> entityType, int maxHealth, double damage, String name) {
        this.entityType = entityType;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage = damage;
        this.name = name;
    }

    public Entity spawn(Location location) {
        Random rng = new Random();
        setSpawnLocation(location);
        Location randomizedSpawn = new Location(location.getWorld(),
                location.getX() + rng.nextInt(10) - 5,
                location.getY() + 1,
                location.getZ() + rng.nextInt(10) - 5);
        setEntity(Objects.requireNonNull(getSpawnLocation().getWorld()).spawn(randomizedSpawn, getEntityType()));
        getEntity().setCustomNameVisible(true);
        setCustomName();
        return getEntity();
    }

    public void onDamage(Player player) {
        setCurrentHealth(getCurrentHealth() - 5);
        if (getDamageDealt().containsKey(player)) {
            getDamageDealt().put(player, getDamageDealt().get(player) + 5);
        } else {
            getDamageDealt().put(player, 5.0);
        }
        if (getCurrentHealth() <= 0) {
            onDeath(player);
        } else {
            setCustomName();
            player.sendMessage("You have damaged a " + getName() + " ! It has " + getCurrentHealth() + " health left.");
        }
    }

    public void onDeath(Player lastHitPlayer) {
        lastHitPlayer.sendMessage("You have killed a " + getName() + "!");
        getDamageDealt().forEach((player, damage) -> {
            Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).getPlayerStats().
                    addMobStatsData(getName().toLowerCase() + "_kills", 1);
            player.sendMessage("You dealt " + damage + " damage to the " + getName() + "!");
            MobDropTable.getDrops(this).forEach((item, amount) -> {
                player.getInventory().addItem(new ItemStack(Item.generateItem(item, amount)));
                player.sendMessage("You have received " + amount + " " + item.getName() + " from the " + getName() + "!");
            });
        });
        if (getSpawner() != null) {
            getSpawner().getCurrentMobs().remove(getEntity());
        }
        Main.mobHandler.removeMob(getEntity());
        for (Entity passenger : getEntity().getPassengers()) {
            passenger.remove();
        }
        getEntity().remove();
    }

    public void setCustomName() {
        for (Entity passenger : getEntity().getPassengers()) {
            passenger.setCustomName(getName() + " " + getCurrentHealth() + "/" + getMaxHealth() + " HP");
        }
        getEntity().setCustomName(getName() + " " + getCurrentHealth() + "/" + getMaxHealth() + " HP");
    }
}
