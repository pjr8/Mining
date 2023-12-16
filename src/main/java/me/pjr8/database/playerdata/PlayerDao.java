package me.pjr8.database.playerdata;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerDao {

    private final Dao<PlayerData, UUID> playerDao;

    public PlayerDao(Dao<PlayerData, UUID> playerDao) {
        this.playerDao = playerDao;
    }

    public PlayerData getPlayerData(UUID uuid) throws SQLException {
        return playerDao.queryForId(uuid);
    }

    public void addUpdatePlayerData(PlayerData playerData) throws SQLException {
        playerDao.createOrUpdate(playerData);
    }

    public boolean playerExists(UUID uuid) throws SQLException {
        return playerDao.idExists(uuid);
    }
}
