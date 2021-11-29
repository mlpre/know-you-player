package ml.minli.controller;

import cn.hutool.core.io.FileUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import ml.minli.api.model.FileType;
import ml.minli.api.model.PlayMedia;
import ml.minli.api.util.*;
import ml.minli.model.MediaListCell;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    @FXML
    public StackPane root;
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
        UiUtil.controllerMap.put(this.getClass().getName(), this);
        //初始化播放器
        playUtil = new PlayUtil(time, timeLabel, playButton, playMediaListView);
        Platform.runLater(() -> {
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
                        playUtil.playMedia(filePath);
                        playButton.setIconCode(FontAwesomeSolid.PAUSE);
                    }
                }
            });
            //注册播放按钮事件
            playButton.setOnMouseClicked(event -> {
                if (playUtil.embeddedMediaPlayer.status().state().intValue() == 0) {
                    if (!playMediaListView.getItems().isEmpty()) {
                        playUtil.playMedia(playMediaListView.getItems().get(0).getFilePath());
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
                MultipleSelectionModel<PlayMedia> selectionModel = playMediaListView.getSelectionModel();
                if (selectionModel != null) {
                    if (selectionModel.getSelectedIndex() - 1 > 0) {
                        playUtil.playMedia(playMediaListView.getItems().get(selectionModel.getSelectedIndex() - 1).getFilePath());
                        playMediaListView.getSelectionModel().select(selectionModel.getSelectedIndex() - 1);
                    } else {
                        playUtil.playMedia(playMediaListView.getItems().get(0).getFilePath());
                        playMediaListView.getSelectionModel().select(0);
                    }
                    playButton.setIconCode(FontAwesomeSolid.PAUSE);
                }
            });
            //下一曲
            forwardButton.setOnMouseClicked(event -> {
                MultipleSelectionModel<PlayMedia> selectionModel = playMediaListView.getSelectionModel();
                if (selectionModel != null) {
                    if (selectionModel.getSelectedIndex() + 1 < playMediaListView.getItems().size()) {
                        playUtil.playMedia(playMediaListView.getItems().get(selectionModel.getSelectedIndex() + 1).getFilePath());
                        playMediaListView.getSelectionModel().select(selectionModel.getSelectedIndex() + 1);
                    } else {
                        playUtil.playMedia(playMediaListView.getItems().get(0).getFilePath());
                        playMediaListView.getSelectionModel().select(0);
                    }
                    playButton.setIconCode(FontAwesomeSolid.PAUSE);
                }
            });
        });
    }

    public void importMedia() throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(LanguageUtil.getValue("play.import.select"));
        File file = directoryChooser.showDialog(root.getScene().getWindow());
        if (file != null && file.isDirectory()) {
            List<File> fileList = FileUtil.loopFiles(file, pathname -> Arrays.stream(FileType.MEDIA)
                    .anyMatch(type -> pathname.getName().toLowerCase().endsWith("." + type)));
            ObservableList<PlayMedia> playMediaList = FXCollections.observableArrayList();
            Map<String, List<PlayMedia>> collect = PlayUtil.playMediaList.stream().collect(Collectors.groupingBy(PlayMedia::getFilePath));
            for (File value : fileList) {
                if (collect.get(value.getAbsolutePath()) != null) {
                    continue;
                }
                PlayMedia playMedia = new PlayMedia();
                playMedia.setFileName(value.getName());
                playMedia.setFilePath(value.getAbsolutePath());
                playMediaList.add(playMedia);
            }
            PlayUtil.playMediaList.addAll(playMediaList);
            PlayUtil.saveList();
            ConfigUtil.store();
            playMediaListView.getItems().addAll(playMediaList);
        }
    }

    public void clearMedia() throws Exception {
        PlayUtil.playMediaList.clear();
        PlayUtil.saveList();
        ConfigUtil.store();
        playMediaListView.getItems().clear();
    }

    public void initProperty() {
        time.setValue(0);
        //初始化列表
        playMediaListView.setCellFactory(list -> new MediaListCell());
    }

}
