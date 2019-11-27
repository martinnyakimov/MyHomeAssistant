package myhomeassistant.server.db;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import myhomeassistant.server.db.model.*;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConnection {
    private ConnectionSource connectionSource;

    public DatabaseConnection() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:myhomeassistant.sqlite");
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public void closeConnection() throws IOException {
        connectionSource.close();
    }

    public void initDatabase() throws SQLException {
        // Create tables if not exists
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Song.class);
        TableUtils.createTableIfNotExists(connectionSource, Sensor.class);
        TableUtils.createTableIfNotExists(connectionSource, SensorsDataLog.class);
        TableUtils.createTableIfNotExists(connectionSource, WeatherSettings.class);
    }
}
