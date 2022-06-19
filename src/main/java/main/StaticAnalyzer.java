package main;

import javafx.scene.control.Alert;
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

    private StaticAnalyzer() {}

    public static AnalyzeResultInfo starter(String... path) {
        log.info("Starting program");

        try {
            if (path != null && StringUtils.isNoneBlank(path[0])) {
                log.info("Entered path: {}", path[0]);

                boolean isPath = Files.isDirectory(new File(path[0]).toPath(), LinkOption.NOFOLLOW_LINKS);
                if (isPath) {
                    AllMetricsStarter starter = AllMetricsStarter.getStarter(path[0]);
                    String res = starter.execute();

                    log.info(res);
                    return AnalyzeResultInfo.builder()
                            .alertType(res.contains("OK") ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING)
                            .folder(path[0] + "MetricResult.html")
                            .errorMessage(res)
                            .result(Objects.equals(res, "OK"))
                            .build();
                }
            }
            log.info("Path to project not found!");
            return AnalyzeResultInfo.builder()
                    .alertType(Alert.AlertType.WARNING)
                    .errorMessage("Path to project not found!")
                    .result(false)
                    .build();

        } catch (Exception e) {
            log.info("Error: {}", e.getMessage(), e);
            return AnalyzeResultInfo.builder()
                    .alertType(Alert.AlertType.ERROR)
                    .errorMessage("Something went wrong!")
                    .result(false)
                    .build();
        }
    }

}

