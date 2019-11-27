package myhomeassistant.server.action;

import myhomeassistant.server.db.model.Song;
import myhomeassistant.server.rest.service.MusicService;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class PlayMusicAction extends Action {

    @Override
    public void implementation(Integer userId) throws SQLException {
        MusicService.stop(); // Stop if there is started song
        List<Song> listOfSongs = MusicService.getAllSongs();

        Random randomIndex = new Random();
        Song selectedSong = listOfSongs.get(randomIndex.nextInt(listOfSongs.size()));
        playSong(selectedSong.getUrl());
    }

    public void playSong(String uri) {
        try {
            Process process = new ProcessBuilder("chromium-browser", uri).start();

            Map<String, Long> processes = this.getProcesses();
            processes.put("music", process.pid());
            this.setProcesses(processes);
        } catch (IOException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }
}
