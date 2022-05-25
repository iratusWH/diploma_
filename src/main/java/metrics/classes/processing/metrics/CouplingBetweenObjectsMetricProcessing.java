package metrics.classes.processing.metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class CouplingBetweenObjectsMetricProcessing extends MetricProcessingImpl {

    private List<Name> imports;

    public CouplingBetweenObjectsMetricProcessing() {
        setMetricName(MetricNameEnum.COUPLING_BETWEEN_OBJECTS_METRIC);
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());

            imports = compilationUnit.getImports()
                    .stream().filter(imp -> !imp.getNameAsString().contains("java."))
                    .map(ImportDeclaration::getName)
                    .distinct()
                    .toList();

            setMetric(String.valueOf(imports.size()));
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("File hasn't find");
        }
    }
}
