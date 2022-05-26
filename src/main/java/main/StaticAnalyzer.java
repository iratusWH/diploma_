package main;

import lombok.extern.slf4j.Slf4j;

/**
 * Основной класс, запускающий анализ проекта
 *
 * @author Маркелов Александр A-07-18
 */
@Slf4j
public class StaticAnalyzer {
    public static void main(String[] args) {
        log.info("Starting program");
        if (args.length != 0) {
            log.info("Entered path: {}", args[0]);

            AllMetricsStarter starter = AllMetricsStarter.getStarter(args[0]);
            starter.execute();
        }
        log.info("Exit program...");
    }
}

