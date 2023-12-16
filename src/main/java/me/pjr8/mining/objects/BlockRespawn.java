package me.pjr8.mining.objects;

import lombok.Data;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Data
public class BlockRespawn {

    private Player player;
    private Block block;
    private Long respawnTime;

    public BlockRespawn(Player player, Block block, Long respawnTime) {
        this.player = player;
        this.block = block;
        this.respawnTime = respawnTime;
    }

}
