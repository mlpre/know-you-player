/*
 * Copyright 2021 Minli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.minli.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import ml.minli.model.PlayMedia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.util.Base64;
import java.util.List;
import java.util.Properties;

/**
 * @author Minli
 */
public class PlayUtil {

    private static final Logger log = LogManager.getLogger(PlayUtil.class.getName());

    public static ObservableList<PlayMedia> playMediaList = FXCollections.observableArrayList();

    public final MediaPlayerFactory mediaPlayerFactory;

    public final EmbeddedMediaPlayer embeddedMediaPlayer;

    public PlayUtil(Slider time, Label timeLabel, FontIcon playButton, ListView<PlayMedia> playMediaListView) {
        time.setMin(0);
        timeLabel.setText("0:0/0:0");
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                Platform.runLater(() -> {
                    long duration = embeddedMediaPlayer.media().info().duration();
                    time.setMax(duration / 1000.0);
                    timeLabel.setText("0:0/" + duration / 1000 / 60 + ":" + duration / 1000 % 60);
                });
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                Platform.runLater(() -> {
                    long duration = embeddedMediaPlayer.media().info().duration();
                    time.setValue(newTime / 1000.0);
                    timeLabel.setText(newTime / 1000 / 60 + ":" + newTime / 1000 % 60 + "/" + duration / 1000 / 60 + ":" + duration / 1000 % 60);
                });
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                Platform.runLater(() -> {
                    playButton.setIconCode(FontAwesomeSolid.PLAY);
                    PlayMedia selectedItem = playMediaListView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        if (selectedItem.getIndex() + 1 <= playMediaListView.getItems().size()) {
                            playMusic(playMediaListView.getItems().get(selectedItem.getIndex() + 1).getFilePath());
                            playMediaListView.getSelectionModel().select(selectedItem.getIndex() + 1);
                        } else {
                            playMusic(playMediaListView.getItems().get(0).getFilePath());
                            playMediaListView.getSelectionModel().select(0);
                        }
                        playButton.setIconCode(FontAwesomeSolid.PAUSE);
                    }
                });
            }
        });
    }

    public void playMusic(String musicPath) {
        try {
            embeddedMediaPlayer.media().play(musicPath);
        } catch (Exception e) {
            log.error("Play Error", e);
        }
    }

    public static ObservableList<PlayMedia> loadList() {
        ObservableList<PlayMedia> observableList = FXCollections.observableArrayList();
        Properties properties = ConfigUtil.properties;
        if (properties != null) {
            String playMediaList = properties.getProperty("playMediaList");
            if (playMediaList != null && !playMediaList.isEmpty()) {
                List<PlayMedia> list = JSON.parseObject(new String(Base64.getDecoder().decode(playMediaList)), new TypeReference<>() {
                });
                observableList.addAll(list);
            }
        }
        playMediaList = observableList;
        return observableList;
    }

    public static void saveList() {
        ConfigUtil.properties.setProperty("playMediaList", Base64.getEncoder().encodeToString(JSON.toJSONString(playMediaList).getBytes()));
    }

}
