package metrics.classes.implementations;

import com.github.javaparser.ast.CompilationUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;
import support.classes.HTMLComponent;
import support.classes.MetricNameEnum;
import support.classes.ResourceFiles;

/**
 * Класс сложной метрики, используется больше одного файла для вычисления
 */
@Slf4j
@Data
public abstract class ComplexMetricProcessingImpl implements MetricProcessing {

    private ResourceFiles fileList; // лист исследуемых файлов
    private Object metric; // результат вычисления метрики
    private MetricNameEnum metricName; // название метрики
    protected CompilationUnit compilationUnit; // юнит для разбора кода на ликсемы вычисления метрик

    @Override
    public HTMLComponent getHtmlComponent(){
        return HTMLComponent.builder()
                .directoryAndFileName(fileList.getProjectPath())
                .metricName(metricName)
                .metric(metric.toString())
                .build();
    }
}
