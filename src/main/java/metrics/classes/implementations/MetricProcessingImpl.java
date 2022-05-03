package metrics.classes.implementations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;

import java.io.File;

@Slf4j
@Data
public abstract class MetricProcessingImpl implements MetricProcessing {

    private String metric;
    private File file;

    @Override
    public void printMetric() {
        log.info(metric);
    }

    @Override
    public void preprocessHTML() {

    }
}
