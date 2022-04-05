package metrics.interfaces;

import java.nio.file.Path;

public interface MetricProcessing {
    String getMetric();
    void setMetric(String metric);
    void processMetric(Path path);
    void printMetric();

}
