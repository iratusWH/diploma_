package metrics.classes.implementations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;
import metrics.interfaces.SimpleMetricProcessing;
import support.classes.HTMLComponent;
import support.classes.MetricNameEnum;

import java.io.File;
import java.util.Objects;

@Slf4j
@Data
public abstract class MetricProcessingImpl implements SimpleMetricProcessing {

    private Object metric;
    private MetricNameEnum metricName;
    private File file;

    @Override
    public HTMLComponent getHTMLComponent() {
        if (Objects.nonNull(file)) {
            return HTMLComponent.builder()
                    .metric(metric.toString())
                    .metricName(metricName)
                    .directoryAndFileName(file.getPath())
                    .build();
        }
        return HTMLComponent.builder()
                .metric(metric.toString())
                .metricName(metricName)
                .build();
    }
}
