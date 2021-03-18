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
package ml.minli.controller;

import cn.hutool.core.io.FileUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import ml.minli.model.FileType;
import ml.minli.model.PlayMedia;
import ml.minli.util.ConfigUtil;
import ml.minli.util.PlayUtil;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Minli
 */
public class MainController implements Initializable {

    @FXML
    public StackPane root;
    @FXML
    public BorderPane borderPane;
    @FXML
    public ToolBar tool;
    @FXML
    public Button importButton;
    @FXML
    public Button clearButton;
    @FXML
    public FontIcon backwardButton;
    @FXML
    public FontIcon playButton;
    @FXML
    public FontIcon forwardButton;
    @FXML
    public Slider time;
    @FXML
    public Label timeLabel;
    @FXML
    public ListView<PlayMedia> playMediaListView;
    @FXML
    public GridPane control;

    public static PlayUtil playUtil;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //初始化播放器
        playUtil = new PlayUtil(time, timeLabel, playButton, playMediaListView);
        //初始化UI属性
        initProperty();
        //载入配置
        ObservableList<PlayMedia> playMediaList = PlayUtil.loadList();
        playMediaListView.getItems().addAll(playMediaList);
        //注册双击播放列表监听器
        playMediaListView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                if (playMediaListView.getSelectionModel().getSelectedItem() != null) {
                    String filePath = playMediaListView.getSelectionModel().getSelectedItem().getFilePath();
                    playUtil.playMusic(filePath);
                    playButton.setIconCode(FontAwesomeSolid.PAUSE);
                }
            }
        });
        //注册播放按钮事件
        playButton.setOnMouseClicked(event -> {
            if (playUtil.embeddedMediaPlayer.status().state().intValue() == 0) {
                if (!playMediaListView.getItems().isEmpty()) {
                    playUtil.playMusic(playMediaListView.getItems().get(0).getFilePath());
                    playMediaListView.getSelectionModel().select(0);
                    playButton.setIconCode(FontAwesomeSolid.PAUSE);
                }
            } else {
                if (playUtil.embeddedMediaPlayer.status().isPlaying()) {
                    playButton.setIconCode(FontAwesomeSolid.PLAY);
                    playUtil.embeddedMediaPlayer.controls().pause();
                } else {
                    playButton.setIconCode(FontAwesomeSolid.PAUSE);
                    playUtil.embeddedMediaPlayer.controls().start();
                }
            }
        });
        //注册播放拖动时间事件
        time.setOnMouseDragged(event -> {
            if (playUtil != null && playUtil.embeddedMediaPlayer.status().isPlaying()) {
                playUtil.embeddedMediaPlayer.controls().setTime((long) time.getValue() * 1000);
            }
        });
        //上一曲
        backwardButton.setOnMouseClicked(event -> {
            PlayMedia selectedItem = playMediaListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.getIndex() - 1 >= 0) {
                    playUtil.playMusic(playMediaListView.getItems().get(selectedItem.getIndex() - 1).getFilePath());
                    playMediaListView.getSelectionModel().select(selectedItem.getIndex() - 1);
                } else {
                    playUtil.playMusic(playMediaListView.getItems().get(0).getFilePath());
                    playMediaListView.getSelectionModel().select(0);
                }
                playButton.setIconCode(FontAwesomeSolid.PAUSE);
            }
        });
        //下一曲
        forwardButton.setOnMouseClicked(event -> {
            PlayMedia selectedItem = playMediaListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.getIndex() + 1 <= playMediaListView.getItems().size()) {
                    playUtil.playMusic(playMediaListView.getItems().get(selectedItem.getIndex() + 1).getFilePath());
                    playMediaListView.getSelectionModel().select(selectedItem.getIndex() + 1);
                } else {
                    playUtil.playMusic(playMediaListView.getItems().get(0).getFilePath());
                    playMediaListView.getSelectionModel().select(0);
                }
                playButton.setIconCode(FontAwesomeSolid.PAUSE);
            }
        });
    }

    public void importMusic() throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择目录");
        File file = directoryChooser.showDialog(root.getScene().getWindow());
        if (file != null && file.isDirectory()) {
            List<File> fileList = FileUtil.loopFiles(file, pathname -> Arrays.stream(FileType.AUDIO)
                    .anyMatch(type -> pathname.getName().endsWith("." + type)));
            ObservableList<PlayMedia> playMediaList = FXCollections.observableArrayList();
            for (int i = 0; i < fileList.size(); i++) {
                PlayMedia playMedia = new PlayMedia();
                playMedia.setFileName(fileList.get(i).getName());
                playMedia.setFilePath(fileList.get(i).getAbsolutePath());
                playMedia.setIndex(i);
                playMediaList.add(playMedia);
            }
            PlayUtil.playMediaList.addAll(playMediaList);
            PlayUtil.saveList();
            ConfigUtil.store();
            playMediaListView.getItems().addAll(playMediaList);
        }
    }

    public void clearMusic() throws Exception {
        PlayUtil.playMediaList.clear();
        PlayUtil.saveList();
        ConfigUtil.store();
        playMediaListView.getItems().clear();
    }

    public void initProperty() {
        time.setValue(0);
        //动态绑定窗口宽度
        Platform.runLater(() -> {
            playMediaListView.minWidthProperty().bind(root.getScene().widthProperty().multiply(0.1));
            playMediaListView.maxWidthProperty().bind(root.getScene().widthProperty().multiply(0.4));
        });
        //鼠标移入left右边界，更换鼠标控件
        playMediaListView.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (event.getX() >= playMediaListView.getWidth() - 5) {
                playMediaListView.setCursor(Cursor.E_RESIZE);
            } else {
                playMediaListView.setCursor(Cursor.DEFAULT);
            }
        });
        //鼠标拖动，自动更新left宽度
        playMediaListView.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (playMediaListView.getCursor() == Cursor.E_RESIZE) {
                if (playMediaListView.prefWidthProperty().isBound()) {
                    playMediaListView.prefWidthProperty().unbind();
                }
                playMediaListView.setPrefWidth(event.getX());
            }
        });
    }

}
