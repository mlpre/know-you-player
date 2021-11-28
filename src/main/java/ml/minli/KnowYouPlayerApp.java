package ml.minli;

import javafx.application.Application;
import ml.minli.ui.MainUi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Minli
 */
public class KnowYouPlayerApp {

    private static final Logger log = LogManager.getLogger(KnowYouPlayerApp.class.getName());

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(log::error);
        Application.launch(MainUi.class, args);
    }

}
