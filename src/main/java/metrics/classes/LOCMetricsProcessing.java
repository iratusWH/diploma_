package metrics.classes;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.io.FileNotFoundException;

@Slf4j
public class LOCMetricsProcessing extends MetricProcessingImpl {

    public LOCMetricsProcessing() {
        setMetricName(MetricNameEnum.LINES_OF_CODE_METRIC);
    }

    @Override
    public void processMetric() {
        try {
            evaluatingMetric();
        } catch(Exception e){
            log.error("LOCMetricProcessing error while getting parse file");
        }
    }

    private void evaluatingMetric() throws FileNotFoundException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
        setMetric(
                compilationUnit.getRange()
                        .map(Range::getLineCount)
                        .map(String::valueOf)
                        .orElse("0")
        );

    }
}
