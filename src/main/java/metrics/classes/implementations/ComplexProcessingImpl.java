package metrics.classes.implementations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;

import java.io.File;
import java.util.List;

@Slf4j
@Data
public abstract class ComplexProcessingImpl implements MetricProcessing {

    private List<File> fileList;
    private String metric;

    @Override
    public void printMetric() {
        log.info(metric);
    }
}
