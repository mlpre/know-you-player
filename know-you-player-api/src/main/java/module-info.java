open module know.you.player.api {
    exports ml.minli.api.model;
    exports ml.minli.api.util;
    requires java.sql;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive org.kordamp.ikonli.javafx;
    requires transitive org.kordamp.ikonli.fontawesome5;
    requires transitive uk.co.caprica.vlcj.javafx;
    requires transitive org.apache.logging.log4j;
    requires uk.co.caprica.vlcj;
    requires hutool.all;
}
