package metrics.classes.processing.metrics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class MaintainabilityIndexMetricProcessing extends MetricProcessingImpl {

    private CyclomaticComplexityMetricProcessing cyclomaticComplexityMetric;
    private HalsteadMetricsProcessing halsteadMetricsProcessing;
    private LOCMetricsProcessing locMetricsProcessing;

    public MaintainabilityIndexMetricProcessing() {
        setMetricName(MetricNameEnum.MAINTAINABILITY_INDEX_METRIC);
    }

    public void setMetrics(CyclomaticComplexityMetricProcessing outerCyclomaticComplexity,
                           HalsteadMetricsProcessing outerHalsteadMetric,
                           LOCMetricsProcessing outerLOCMetric){

        cyclomaticComplexityMetric = outerCyclomaticComplexity;
        halsteadMetricsProcessing = outerHalsteadMetric;
        locMetricsProcessing = outerLOCMetric;
    }

    @Override
    public void processMetric() {
        double metric = (171.0 - 5.2 * Math.log(halsteadMetricsProcessing.getVocabulary()) - 0.23 * getMaxCyclomaticComplexity() - 16.2 * Math.log(getLOC())) * 100.0 / 171.0;
        setMetric(metric);
    }

    public Integer getMaxCyclomaticComplexity() {
        List<Integer> cyclomaticComplexitiesOfCMethods = new ArrayList<>(cyclomaticComplexityMetric.getMethodNamesWithOperatorsCount().values());
        if (cyclomaticComplexitiesOfCMethods.isEmpty())
            return 0;

        return Collections.max(cyclomaticComplexitiesOfCMethods);
    }

    public Integer getLOC() {
        return (Integer) locMetricsProcessing.getMetric();
    }
}
