package me.pjr8.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import me.pjr8.database.playerdata.PlayerData;

import java.sql.SQLException;
import java.util.UUID;

@Getter
public class Database {

    private final Dao<PlayerData, UUID> playerDao;

    public Database() throws SQLException {
        String databaseURL = "jdbc:mysql://root:password@localhost/mining_project";
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseURL);
        playerDao = DaoManager.createDao(connectionSource, PlayerData.class);
        TableUtils.createTableIfNotExists(connectionSource, PlayerData.class);
    }
}
