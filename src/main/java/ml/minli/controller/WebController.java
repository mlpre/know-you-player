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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import ml.minli.model.PlayMedia;
import ml.minli.util.ConfigUtil;
import ml.minli.util.ResourceUtil;
import ml.minli.util.UiUtil;
import ml.minli.util.PlayUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Minli
 */
public class WebController implements Initializable {

    @FXML
    public ToolBar tool;
    @FXML
    public TextField url;
    @FXML
    public TextField audioNameRule;
    @FXML
    public WebView web;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UiUtil.controllerMap.put(this.getClass().getName(), this);
    }

    public void view() {
        WebEngine webEngine = web.getEngine();
        if (url.getText() != null && !url.getText().isBlank()) {
            String result = url.getText();
            if (!result.startsWith("http://") && !result.startsWith("https://")) {
                result = "http://" + result;
            }
            webEngine.setUserDataDirectory(new File(ResourceUtil.cachePath));
            webEngine.load(result);
        }
    }

    public synchronized void analyseAudio() throws Exception {
        Document document = web.getEngine().getDocument();
        NodeList audio = document.getElementsByTagName("audio");
        if (audio.getLength() > 0) {
            NamedNodeMap attributes = audio.item(0).getAttributes();
            if (attributes.getLength() > 0) {
                Node result = attributes.getNamedItem("src");
                if (result != null) {
                    String src = result.getTextContent();
                    String audioName = analyseAudioName(src);
                    PlayMedia playMedia = new PlayMedia();
                    playMedia.setFilePath(src);
                    playMedia.setFileName(audioName);
                    PlayUtil.playMediaList.add(playMedia);
                    PlayUtil.saveList();
                    ConfigUtil.store();
                    MainController mainController = (MainController) UiUtil.controllerMap.get(MainController.class.getName());
                    mainController.playMediaListView.getItems().add(playMedia);
                }
            }
        }
    }

    public String analyseAudioName(String src) {
        if (audioNameRule.getText() == null || audioNameRule.getText().isBlank()) {
            return src;
        }
        String audioNameRuleString = audioNameRule.getText();
        String audioName = null;
        String[] strings = audioNameRuleString.split(";");
        Document document = web.getEngine().getDocument();
        for (String string : strings) {
            String[] rule = string.split(",");
            NodeList span = document.getElementsByTagName(rule[0]);
            for (int i = 0; i < span.getLength(); i++) {
                Node item = span.item(i);
                Node type = item.getAttributes().getNamedItem(rule[1]);
                if (type != null && type.getTextContent().equals(rule[2])) {
                    audioName = (audioName == null ? "" : audioName) + item.getTextContent();
                    break;
                }
            }
        }
        if (audioName == null) {
            return src;
        }
        return audioName;
    }

}
