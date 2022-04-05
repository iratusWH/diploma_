package metrics.classes.implementations;

import metrics.interfaces.MetricProcessing;

import java.nio.file.Path;

public abstract class MetricProcessingImpl implements MetricProcessing {

    private static String METRIC;

    @Override
    public String getMetric() {
        return METRIC;
    }

    @Override
    public void setMetric(String metric){
        METRIC = metric;
    }

    @Override
    public void processMetric(Path path) {

    }

    @Override
    public void printMetric() {
        System.out.println(METRIC);
    }
}
