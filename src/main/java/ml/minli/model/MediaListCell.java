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
package ml.minli.model;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import ml.minli.controller.MainController;
import ml.minli.util.*;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.UUID;

/**
 * @author Minli
 */
public class MediaListCell extends ListCell<PlayMedia> {

    @Override
    protected void updateItem(PlayMedia playMedia, boolean empty) {
        super.updateItem(playMedia, empty);
        if (!empty) {
            Label label = new Label(playMedia.getFileName());
            FontIcon labelFontIcon = new FontIcon(FontAwesomeSolid.MUSIC);
            labelFontIcon.setIconColor(Paint.valueOf("#5264ae"));
            label.setGraphic(labelFontIcon);
            Button delButton = new Button();
            FontIcon delFontIcon = new FontIcon(FontAwesomeSolid.TIMES_CIRCLE);
            delFontIcon.setIconColor(Paint.valueOf("#dc3545"));
            delButton.setGraphic(delFontIcon);
            delButton.setOnAction(event -> {
                try {
                    PlayMedia currPlayMedia = ((MediaListCell) ((Button) event.getTarget()).getParent().getParent()).getItem();
                    MainController mainController = (MainController) UiUtil.controllerMap.get(MainController.class.getName());
                    PlayUtil.playMediaList.remove(currPlayMedia);
                    PlayUtil.saveList();
                    ConfigUtil.store();
                    mainController.playMediaListView.getItems().remove(currPlayMedia);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Button downloadButton = new Button();
            FontIcon downloadFontIcon = new FontIcon(FontAwesomeSolid.FILE_DOWNLOAD);
            downloadFontIcon.setIconColor(Paint.valueOf("#28a745"));
            downloadButton.setGraphic(downloadFontIcon);
            downloadButton.setOnAction(event -> {
                try {
                    MediaListCell mediaListCell = ((MediaListCell) ((Button) event.getTarget()).getParent().getParent());
                    PlayMedia currPlayMedia = mediaListCell.getItem();
                    String filePath = currPlayMedia.getFilePath();
                    new Thread(new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            String fileName = currPlayMedia.getFileName();
                            String suffix = filePath.substring(filePath.lastIndexOf(".")).length() <= 5 ? filePath.substring(filePath.lastIndexOf(".")) : ".mp3";
                            if (currPlayMedia.getFileName().startsWith("http://") || currPlayMedia.getFileName().startsWith("https://")) {
                                fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
                            } else {
                                fileName = fileName + suffix;
                            }
                            DownloadUtil.download(filePath, ResourceUtil.downloadPath + File.separator + fileName);
                            PlayMedia newPlayMedia = PlayUtil.playMediaList.get(mediaListCell.getIndex());
                            newPlayMedia.setFilePath(ResourceUtil.downloadPath + File.separator + fileName);
                            newPlayMedia.setFileName(fileName);
                            PlayUtil.saveList();
                            ConfigUtil.store();
                            MainController mainController = (MainController) UiUtil.controllerMap.get(MainController.class.getName());
                            mainController.playMediaListView.refresh();
                            return null;
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            HBox hBox = new HBox();
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER_LEFT);
            if (playMedia.getFilePath().startsWith("http://") || playMedia.getFilePath().startsWith("https://")) {
                hBox.getChildren().addAll(downloadButton, delButton, label);
            } else {
                hBox.getChildren().addAll(delButton, label);
            }
            this.setGraphic(hBox);
        } else {
            this.setGraphic(null);
        }
        this.setText(null);
    }

}
