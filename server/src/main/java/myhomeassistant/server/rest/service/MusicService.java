package myhomeassistant.server.rest.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import myhomeassistant.server.action.ActionUtil;
import myhomeassistant.server.action.PlayMusicAction;
import myhomeassistant.server.action.StopMusicAction;
import myhomeassistant.server.db.DatabaseConnection;
import myhomeassistant.server.db.model.Song;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MusicService {
    private static Dao<Song, String> musicDao;

    static {
        try {
            musicDao = DaoManager.createDao(new DatabaseConnection().getConnectionSource(), Song.class);
        } catch (SQLException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }

    public static List<Song> getAllSongs() throws SQLException {
        QueryBuilder<Song, String> builder = musicDao.queryBuilder();
        return musicDao.query(builder.prepare());
    }

    public static Song getSongById(Integer id) throws SQLException {
        return musicDao.queryForId(String.valueOf(id));
    }

    public static void addSong(String title, String url) throws SQLException, IOException {
        if (title == null || title.equals("")) {
            Document doc = Jsoup.connect(url).get();
            title = doc.title().replace(" - YouTube", ""); // Remove website suffix
        }

        Song song = new Song();
        song.setUrl(url);
        song.setTitle(title);
        musicDao.create(song);
    }

    public static void playSingle(Integer id) throws SQLException {
        Song song = getSongById(id);
        stop(); // Stop previous songs before starting the new
        PlayMusicAction playMusicAction = new PlayMusicAction();
        playMusicAction.playSong(song.getUrl());
    }

    public static void stop() {
        StopMusicAction stopMusicAction = new StopMusicAction();
        stopMusicAction.implementation(null);
    }

    public static Boolean activeSong() {
        ActionUtil actionUtil = new ActionUtil();
        return actionUtil.getActionProcesses().get("music") != null;
    }

    public static void deleteSong(Integer id) throws SQLException {
        musicDao.deleteById(String.valueOf(id));
    }
}
