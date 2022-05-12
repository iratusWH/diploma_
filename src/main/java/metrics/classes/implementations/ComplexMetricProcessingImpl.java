package metrics.classes.implementations;

import com.github.javaparser.ast.CompilationUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;
import support.classes.HTMLComponent;
import support.classes.MetricNameEnum;
import support.classes.ResourceFiles;

@Slf4j
@Data
public abstract class ComplexMetricProcessingImpl implements MetricProcessing {

    private ResourceFiles fileList;
    private String metric;
    private MetricNameEnum metricName;
    protected CompilationUnit compilationUnit;
    private String fileName;

    @Override
    public void printMetric() {
        log.info(metric);
    }

    @Override
    public HTMLComponent getHTMLComponent(){
        return HTMLComponent.builder()
                .metric(metric)
                .metricName(metricName)
                .directoryAndFileName(fileName)
                .build();
    }
}
