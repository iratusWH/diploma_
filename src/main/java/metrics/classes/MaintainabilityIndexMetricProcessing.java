package metrics.classes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.ComplexMetricProcessingImpl;

@Data
@Slf4j
public class MaintainabilityIndexMetricProcessing extends ComplexMetricProcessingImpl {

    private final CyclomaticComplexityMetricProcessing cyclomaticComplexity;
    private final LOCMetricsProcessing locMetric;


    @Override
    public void processMetric() {

    }

    @Override
    public void preprocessOutput() {

    }
}
