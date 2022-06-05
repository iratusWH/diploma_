package metrics.classes.implementations;

import com.github.javaparser.ast.CompilationUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.SimpleMetricProcessing;
import support.classes.MetricNameEnum;

/**
 * Класс обычной метрики, исследуемая метрика основана на одном файле
 */
@Slf4j
@Data
public abstract class MetricProcessingImpl implements SimpleMetricProcessing {

    private Object metric; // результирующая метрика
    private MetricNameEnum metricName; // название метрики
    private CompilationUnit file; // исследумый файл
}
