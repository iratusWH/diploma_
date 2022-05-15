package metrics.classes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class MaintainabilityIndexMetricProcessing extends MetricProcessingImpl {

    private final CyclomaticComplexityMetricProcessing cyclomaticComplexityMetric;
    private final HalsteadMetricsProcessing halsteadMetricsProcessing;
    private final LOCMetricsProcessing locMetricsProcessing;

    public MaintainabilityIndexMetricProcessing(CyclomaticComplexityMetricProcessing outerCyclomaticComplexity,
                                                HalsteadMetricsProcessing outerHalsteadMetric,
                                                LOCMetricsProcessing outerLOCMetric) {
        setMetricName(MetricNameEnum.MAINTAINABILITY_INDEX_METRIC);

        cyclomaticComplexityMetric = outerCyclomaticComplexity;
        halsteadMetricsProcessing = outerHalsteadMetric;
        locMetricsProcessing = outerLOCMetric;
    }

    @Override
    public void processMetric() {

        setMetric((171.0 - 5.2 * Math.log(halsteadMetricsProcessing.getVocabulary()) - 0.23 * getMaxCyclomaticComplexity() - 16.2 * getLOC()) * 100.0 / 171.0);
    }

    public Integer getMaxCyclomaticComplexity() {
        List<Integer> cyclomaticComplexitiesOfCMethods = cyclomaticComplexityMetric.getMethodNamesWithOperatorsCount().values().stream().toList();
        return Collections.max(cyclomaticComplexitiesOfCMethods);
    }

    public Integer getLOC() {
        return Integer.getInteger(
                String.valueOf(locMetricsProcessing.getMetric()));
    }
}
