package me.pjr8.database.player;

import lombok.Data;
import me.pjr8.forge.objects.ForgeData;
import me.pjr8.mining.objects.PickaxeData;
import me.pjr8.rank.GameRank;
import me.pjr8.rank.ServerRank;


import java.util.UUID;

@Data
public class PlayerData {

    private UUID uuid;

    private String name;

    private Long lastJoin;

    private ServerRank serverRank;

    private GameRank gameRank;

    private PickaxeData pickaxeData;

    private ForgeData forgeData;

    private PlayerStats playerStats;
}
