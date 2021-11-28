package ml.minli.api;

import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import ml.minli.api.model.PlayMedia;

public interface AnalyseService {

    void analyse(ToolBar toolBar, ListView<PlayMedia> playMediaListView);

}
