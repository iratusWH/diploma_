package metrics.interfaces;

import java.io.File;

public interface SimpleMetricProcessing extends MetricProcessing {
    Object getMetric();
    void setFile(File file);
}
