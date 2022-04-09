package metrics.classes.implementations;

import com.github.javaparser.ast.CompilationUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;

import java.io.File;
import java.util.List;

@Slf4j
@Data
public abstract class ComplexMetricProcessingImpl implements MetricProcessing {

    private List<File> fileList;
    private String metric;
    protected CompilationUnit compilationUnit;

    @Override
    public void printMetric() {
        log.info(metric);
    }
}
