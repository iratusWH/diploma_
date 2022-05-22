package metrics.classes.implementations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.SimpleMetricProcessing;
import support.classes.MetricNameEnum;

import java.io.File;

@Slf4j
@Data
public abstract class MetricProcessingImpl implements SimpleMetricProcessing {

    private Object metric;
    private MetricNameEnum metricName;
    private File file;
}
