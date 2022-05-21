package main;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
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

