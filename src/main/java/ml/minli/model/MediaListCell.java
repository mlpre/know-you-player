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

import javafx.scene.control.ListCell;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Minli
 */
public class MediaListCell extends ListCell<PlayMedia> {

    @Override
    protected void updateItem(PlayMedia playMedia, boolean empty) {
        super.updateItem(playMedia, empty);
        if (!empty) {
            FontIcon fontIcon = new FontIcon(FontAwesomeSolid.MUSIC);
            fontIcon.setIconColor(Paint.valueOf("#5264ae"));
            this.setGraphic(fontIcon);
            this.setText(playMedia.getFileName());
        }
    }

}
