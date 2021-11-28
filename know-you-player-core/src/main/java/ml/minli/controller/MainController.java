package ml.minli.controller;

import cn.hutool.core.io.FileUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ml.minli.api.model.FileType;
import ml.minli.api.model.PlayMedia;
import ml.minli.api.util.*;
import ml.minli.model.MediaListCell;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import uk.co.caprica.vlcj.javafx.view.ResizableImageView;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

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
    public Button onlinePlayButton;
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

    public ResizableImageView resizableImageView;

    public ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UiUtil.controllerMap.put(this.getClass().getName(), this);
        this.imageView = new ImageView();
        this.resizableImageView = new ResizableImageView(imageView);
        borderPane.setCenter(this.resizableImageView);
        //初始化播放器
        playUtil = new PlayUtil(imageView, time, timeLabel, playButton, playMediaListView);
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
            //全屏事件
            imageView.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    Parent parent = imageView.getParent().getParent();
                    if (imageView.getImage() != null && parent instanceof BorderPane) {
                        Stage mainStage = (Stage) root.getScene().getWindow();
                        Stage newStage = new Stage();
                        newStage.initStyle(StageStyle.UNDECORATED);
                        newStage.setScene(new Scene(new StackPane(resizableImageView)));
                        newStage.setFullScreen(true);
                        mainStage.close();
                        newStage.show();
                        newStage.fullScreenProperty().addListener((observableValue, oldValue, newValue) -> {
                            if (!newValue) {
                                newStage.close();
                                borderPane.setCenter(this.resizableImageView);
                                mainStage.show();
                            }
                        });
                    }
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
                    .anyMatch(type -> pathname.getName().endsWith("." + type)));
            ObservableList<PlayMedia> playMediaList = FXCollections.observableArrayList();
            for (File value : fileList) {
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
        //动态绑定窗口宽度
        playMediaListView.minWidthProperty().bind(root.getScene().widthProperty().multiply(0.1));
        playMediaListView.prefWidthProperty().bind(root.getScene().widthProperty().multiply(0.5));
        playMediaListView.maxWidthProperty().bind(root.getScene().widthProperty().multiply(0.9));
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
        //初始化列表
        playMediaListView.setCellFactory(list -> new MediaListCell());
    }

    public void onlinePlay() {
        Platform.runLater(() -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            String path = clipboard.getContent(DataFormat.PLAIN_TEXT).toString();
            try {
                PlayMedia playMedia = new PlayMedia();
                playMedia.setFileName(path);
                playMedia.setFilePath(path);
                PlayUtil.playMediaList.add(playMedia);
                PlayUtil.saveList();
                ConfigUtil.store();
                playMediaListView.getItems().add(playMedia);
            } catch (Exception e) {
                e.printStackTrace();
            }
            playUtil.playMedia(path);
        });
    }

}
