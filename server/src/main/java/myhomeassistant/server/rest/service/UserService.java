package myhomeassistant.server.rest.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import myhomeassistant.server.db.DatabaseConnection;
import myhomeassistant.server.db.model.User;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static Dao<User, String> userDao;

    static {
        try {
            userDao = DaoManager.createDao(new DatabaseConnection().getConnectionSource(), User.class);
        } catch (SQLException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userDao.forEach(users::add);

        return users;
    }

    public static List<User> getAllUsersWithEmails() throws SQLException {
        return userDao.query(userDao.queryBuilder().where().isNotNull("email").prepare());
    }

    public static User getUserById(Integer id) throws SQLException {
        return userDao.queryForId(String.valueOf(id));
    }

    public static User getUserByUUID(String uuid) {
        try {
            return userDao.queryForEq("uuid", uuid).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static void createUser(String name, String email, String UUID) throws SQLException {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUuid(UUID);

        userDao.create(user);
    }

    public static void updateUser(Integer id, String name, String email, String UUID) throws SQLException {
        User user = getUserById(id);
        user.setName(name);
        user.setEmail(email);
        user.setUuid(UUID);
        userDao.update(user);
    }

    public static void deleteUser(Integer id) throws SQLException {
        userDao.deleteById(String.valueOf(id));
    }
}
