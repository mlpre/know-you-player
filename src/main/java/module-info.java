/**
 * @author Minli
 */
open module know.you.player {
    uses ml.minli.api.AnalyseService;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires know.you.player.api;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires hutool.core;
    requires fastjson;
    requires org.apache.logging.log4j;
}