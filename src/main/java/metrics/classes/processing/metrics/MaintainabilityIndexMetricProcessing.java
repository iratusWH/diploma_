package metrics.classes.processing.metrics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс вычисляющий метрику ремонтопригодности программного кода
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class MaintainabilityIndexMetricProcessing extends SimpleMetricProcessingImpl {

    private CyclomaticComplexityMetricProcessing cyclomaticComplexityMetric; // класс метрики цикломатической сложности кода
    private HalsteadMetricsProcessing halsteadMetricsProcessing; // класс метрик Холстеда
    private LOCMetricsProcessing locMetricsProcessing; // класс метрики количества строк кода

    public MaintainabilityIndexMetricProcessing() {
        setMetricName(MetricNameEnum.MAINTAINABILITY_INDEX_METRIC);
    }

    // получение всех метрик, которые необходимы для расчетов
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

    // получение максимальной метрики цикломатической сложности функции из всех в исследуемом классе
    private Integer getMaxCyclomaticComplexity() {
        List<Integer> cyclomaticComplexitiesOfCMethods = new ArrayList<>(cyclomaticComplexityMetric.getMethodNamesWithOperatorsCount().values());
        if (cyclomaticComplexitiesOfCMethods.isEmpty())
            return 0;

        return Collections.max(cyclomaticComplexitiesOfCMethods);
    }

    // получение метрики количества строк кода из класса метрики LOC
    public Integer getLOC() {
        return (Integer) locMetricsProcessing.getMetric();
    }
}
