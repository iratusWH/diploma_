package metrics.classes.implementations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;
import support.classes.HTMLComponent;
import support.classes.MetricNameEnum;

import java.io.File;

@Slf4j
@Data
public abstract class MetricProcessingImpl implements MetricProcessing {

    private Object metric;
    private MetricNameEnum metricName;
    private File file;

    @Override
    public void printMetric() {
        log.info(metric.toString());
    }

    @Override
    public HTMLComponent getHTMLComponent() {
        return HTMLComponent.builder().metric(metric.toString())
                .metricName(metricName)
                .directoryAndFileName(file.getPath())
                .build();
    }
}
