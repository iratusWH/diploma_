package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * Основной класс, запускающий анализ проекта
 *
 * @author Маркелов Александр A-07-18
 */
@Slf4j
public class StaticAnalyzer {

    public static void main(String[] args) {
        log.info("Starting program");

        if (args.length == 1) {
            log.info("Entered path: {}", args[0]);

            AllMetricsStarter starter = AllMetricsStarter.getStarter(args[0]);
            starter.execute();
        } else {
            log.info("Path to project not found!");
        }
        log.info("Exit program...");
    }

}

