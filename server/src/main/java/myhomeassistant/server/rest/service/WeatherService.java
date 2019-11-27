package myhomeassistant.server.rest.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import myhomeassistant.server.db.DatabaseConnection;
import myhomeassistant.server.db.model.WeatherSettings;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;

import java.sql.SQLException;
import java.util.List;

public class WeatherService {
    private static Dao<WeatherSettings, String> weatherDao;

    static {
        try {
            weatherDao = DaoManager.createDao(new DatabaseConnection().getConnectionSource(), WeatherSettings.class);
        } catch (SQLException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }

    public static WeatherSettings getSettings() throws SQLException {
        QueryBuilder<WeatherSettings, String> builder = weatherDao.queryBuilder();
        return weatherDao.queryForFirst(builder.prepare());
    }

    public static void updateSettings(String city, String units) throws SQLException {
        TableUtils.clearTable(new DatabaseConnection().getConnectionSource(), WeatherSettings.class);

        WeatherSettings settings = new WeatherSettings();
        settings.setCity(city);
        settings.setUnits(units);
        weatherDao.create(settings);
    }
}
