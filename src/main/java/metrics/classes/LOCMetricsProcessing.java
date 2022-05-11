package metrics.classes;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;

@Slf4j
public class LOCMetricsProcessing extends MetricProcessingImpl {


    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            setMetric(
                    compilationUnit.getRange()
                            .map(Range::getLineCount)
                            .map(String::valueOf)
                            .orElse("0")
            );

        } catch(Exception e){
            log.error("LOCMetricProcessing error while getting parse file");
        }
    }

    @Override
    public void preprocessOutput() {

    }
}
