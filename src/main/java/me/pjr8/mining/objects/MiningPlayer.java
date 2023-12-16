package me.pjr8.mining.objects;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Data
public class MiningPlayer {

    private Player player;

    private HashMap<Location, BlockHolder> currentOres;

    public MiningPlayer(Player player) {
        this.player = player;
        this.currentOres = new HashMap<Location, BlockHolder>();
    }

    public void addCurrentOres(Location location, BlockHolder blockHolder) {
        currentOres.put(location, blockHolder);
    }

    public void removeCurrentOre(Location location) {
        currentOres.remove(location);
    }

}
