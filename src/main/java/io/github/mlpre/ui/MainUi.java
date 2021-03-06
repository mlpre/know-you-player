package io.github.mlpre.ui;

import io.github.mlpre.util.ConfigUtil;
import io.github.mlpre.util.LanguageUtil;
import io.github.mlpre.util.ResourceUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

public class MainUi extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        ConfigUtil.initConfig();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent main = FXMLLoader.load(ResourceUtil.getResource("fxml/main.fxml"), LanguageUtil.resourceBundle);
        stage.setTitle(LanguageUtil.getValue("play.title"));
        Scene scene = new Scene(main, 400, 600);
        scene.getStylesheets().add(ResourceUtil.getExternalForm("css/main.css"));
        stage.getIcons().add(new Image(ResourceUtil.getInputStream("img/logo.png")));
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(600);
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(ResourceUtil.getInputStream("img/logo.png")));
            alert.setTitle(LanguageUtil.getValue("play.exit"));
            FontIcon fontIcon = new FontIcon(FontAwesomeSolid.MUSIC);
            fontIcon.setIconColor(Paint.valueOf("#5264AE"));
            fontIcon.setIconSize(50);
            alert.setGraphic(fontIcon);
            alert.setContentText(LanguageUtil.getValue("play.isExit"));
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    Platform.exit();
                    System.exit(0);
                } else {
                    event.consume();
                }
            });
        });
        stage.show();
    }

}
