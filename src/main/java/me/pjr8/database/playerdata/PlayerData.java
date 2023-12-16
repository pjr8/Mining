package me.pjr8.database.playerdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import me.pjr8.mining.objects.PickaxeData;


import java.util.UUID;

@Data
@DatabaseTable(tableName = "players")
public class PlayerData {

    @DatabaseField(unique = true, id = true, columnDefinition = "VARCHAR(36)")
    private UUID uuid;

    @DatabaseField(columnDefinition = "VARCHAR(16)")
    private String name;

    @DatabaseField(columnName = "last_join", columnDefinition = "BIGINT")
    private Long lastJoin;

    @DatabaseField(columnName = "pickaxe_data", columnDefinition = "TEXT")
    private String pickaxeDataSerialized;

    private PickaxeData pickaxeData;

    public PlayerData() {
    }

}
