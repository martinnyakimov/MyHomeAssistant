package myhomeassistant.server.rest.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import myhomeassistant.server.db.DatabaseConnection;
import myhomeassistant.server.db.model.Sensor;
import myhomeassistant.server.db.model.SensorsDataLog;
import myhomeassistant.server.db.model.User;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SecurityLogService {
    private static Dao<Sensor, Integer> sensorsDao;
    private static Dao<SensorsDataLog, Long> dataLogDao;

    static {
        try {
            sensorsDao = DaoManager.createDao(new DatabaseConnection().getConnectionSource(), Sensor.class);
            dataLogDao = DaoManager.createDao(new DatabaseConnection().getConnectionSource(), SensorsDataLog.class);
        } catch (SQLException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }

    public static List<SensorsDataLog> getLog() throws SQLException {
        QueryBuilder<Sensor, Integer> sensors = sensorsDao.queryBuilder();
        QueryBuilder<SensorsDataLog, Long> query = dataLogDao.queryBuilder();
        query.join(sensors).orderBy("id", false);

        return query.query();
    }

    public static void add() throws SQLException {
        SensorsDataLog data = new SensorsDataLog();
        Sensor securitySensor = sensorsDao.queryForId(1);

        // Get last log item and don't insert new if there is one with the same date and time
        QueryBuilder<SensorsDataLog, Long> builder = dataLogDao.queryBuilder().limit(1L).orderBy("id", false);
        try {
            SensorsDataLog lastTLogItem = dataLogDao.query(builder.prepare()).get(0);

            Date date = new Date(new Timestamp(Long.parseLong(lastTLogItem.getTimestamp()) * 1000).getTime());
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            if (!dateFormat.format(date).equals(dateFormat.format(now))) {
                // Update log
                insertDataItem(data, securitySensor);
                // Get user with emails
                List<User> usersWithEmails = UserService.getAllUsersWithEmails();
                // Send email notification to all users with emails
                usersWithEmails.forEach(user -> {
                    try {
                        new ProcessBuilder("bash", "-c", MainUtil.sendEmailNotification(user.getEmail(), dateFormat.format(now))).start();
                    } catch (IOException e) {
                    }
                });
            }
        } catch (IndexOutOfBoundsException e) {
            insertDataItem(data, securitySensor);
        }
    }

    private static void insertDataItem(SensorsDataLog data, Sensor securitySensor) throws SQLException {
        data.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000L));
        data.setSensor(securitySensor);
        dataLogDao.create(data);
    }

    public static void deleteLogItem(Long id) throws SQLException {
        dataLogDao.deleteById(id);
    }
}
