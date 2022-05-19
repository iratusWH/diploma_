package metrics.classes.processing.metrics;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.io.FileNotFoundException;

public class CyclesComplexityMetricProcessing extends MetricProcessingImpl {

    CyclesComplexityMetricProcessing() {
        setMetricName(MetricNameEnum.CYCLES_COMPLEXITY_METRIC);
    }

    @Override
    public void processMetric() throws FileNotFoundException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());


    }

}
