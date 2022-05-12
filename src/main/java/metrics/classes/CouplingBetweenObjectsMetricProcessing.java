package metrics.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
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

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class CouplingBetweenObjectsMetricProcessing extends MetricProcessingImpl {

    private List<Name> imports;

    public CouplingBetweenObjectsMetricProcessing(){
        setMetricName(MetricNameEnum.COUPLING_BETWEEN_OBJECTS_METRIC);
    }

    @Override
    public void processMetric() {
        try {
            imports = new ArrayList<>();
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());

            addNotStandardImports(compilationUnit);
            distinctImports();
            setMetric(String.valueOf(imports.size()));
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("File isn't find");
        }
    }

    private void addNotStandardImports(CompilationUnit compilationUnit){
        compilationUnit.getImports().forEach(imp0rt -> {
            if (!imp0rt.getNameAsString().contains("java.")) imports.add(imp0rt.getName());
        });
    }

    private void distinctImports(){
        Set<Name> distinctImports = new HashSet<>(imports);
        imports = distinctImports.stream().toList();
        log.info("CBO imports: {}", distinctImports);
    }
}
