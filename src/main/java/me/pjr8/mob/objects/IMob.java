package me.pjr8.mob.objects;

import me.pjr8.Item.Item;
import me.pjr8.Main;
import me.pjr8.mob.MobDropTable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

public interface IMob {

    void onAttack(Player player);
    void setSpawnLocation(Location location);
    String getName();
    void setName(String name);
    Location getSpawnLocation();
    void setCurrentHealth(double health);
    double getCurrentHealth();
    void setMaxHealth(int health);
    int getMaxHealth();
    void setDamage(double damage);
    double getDamage();
    void setSpeed(int speed);
    int getSpeed();
    HashMap<Player, Double> getDamageDealt();
    Entity getEntity();
    void setEntity(Entity entity);
    Class<? extends Entity> getEntityType();
    SpawnerData getSpawner();
    void setSpawner(SpawnerData spawner);
    default Entity spawn(Location location) {
        Random rng = new Random();
        setSpawnLocation(location);
        Location randomizedSpawn = new Location(location.getWorld(),
                location.getX() + rng.nextInt(10) - 5,
                location.getY() + 1,
                location.getZ() + rng.nextInt(10) - 5);
        setEntity(getSpawnLocation().getWorld().spawn(randomizedSpawn, getEntityType()));
        getEntity().setCustomNameVisible(true);
        setCustomName();
        return getEntity();
    }

    default void onDamage(Player player) {
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

    default void onDeath(Player lastHitPlayer) {
        lastHitPlayer.sendMessage("You have killed a " + getName() + "!");
        getDamageDealt().forEach((player, damage) -> {
/*            Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).getPlayerStats().
                    addMobInfoData(getName().toLowerCase() + "_kills", 1);*/
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

    default void setCustomName() {
        for (Entity passenger : getEntity().getPassengers()) {
            passenger.setCustomName(getName() + " " + getCurrentHealth() + "/" + getMaxHealth() + " HP");
        }
        getEntity().setCustomName(getName() + " " + getCurrentHealth() + "/" + getMaxHealth() + " HP");
    }
}
