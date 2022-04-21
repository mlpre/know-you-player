package io.github.mlpre;

import javafx.application.Application;
import io.github.mlpre.ui.MainUi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KnowYouPlayerApp {

    private static final Logger log = LogManager.getLogger(KnowYouPlayerApp.class.getName());

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(log::error);
        Application.launch(MainUi.class, args);
    }

}
