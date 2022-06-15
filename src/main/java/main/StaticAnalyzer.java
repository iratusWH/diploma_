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
import org.apache.commons.lang3.StringUtils;
import support.classes.AnalyzeResultInfo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Objects;

/**
 * Основной класс, запускающий анализ проекта
 *
 * @author Маркелов Александр A-07-18
 */
@Slf4j
public class StaticAnalyzer {

    public AnalyzeResultInfo starter(String[] args) {
        log.info("Starting program");

        if (args.length == 1 && args[0] != null && StringUtils.isNoneBlank(args[0])) {
            log.info("Entered path: {}", args[0]);

            boolean isPath = Files.isDirectory(new File(args[0]).toPath(), LinkOption.NOFOLLOW_LINKS);
            if (isPath) {
                AllMetricsStarter starter = AllMetricsStarter.getStarter(args[0]);
                String res = starter.execute();

                log.info(res);
                return AnalyzeResultInfo.builder()
                        .errorMessage(res)
                        .result(Objects.equals(res, "OK"))
                        .build();
            }
        }
        log.info("Path to project not found!");
        return AnalyzeResultInfo.builder()
                .errorMessage("Path to project not found!")
                .result(false)
                .build();
    }

}

