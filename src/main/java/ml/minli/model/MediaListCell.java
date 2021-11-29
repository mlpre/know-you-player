package ml.minli.model;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import ml.minli.util.*;
import ml.minli.controller.MainController;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

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
            HBox hBox = new HBox();
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(delButton, label);
            this.setGraphic(hBox);
        } else {
            this.setGraphic(null);
        }
        this.setText(null);
    }

}
