package metrics.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class CouplingBetweenObjectsMetricProcessing extends MetricProcessingImpl {

    private static final String COUPLING_BETWEEN_OBJECTS = "Coupling Between Objects";

    private List<Name> imports;
    private String classDeclaration;

    @Override
    public void processMetric() {
        try {
            imports = new ArrayList<>();
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            compilationUnit.getImports().forEach(imp0rt -> imports.add(imp0rt.getName()));

            classDeclaration = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).map(ClassOrInterfaceDeclaration::getNameAsString).orElse("UnknownClass");
            Set<Name> distinctImports = new HashSet<>(imports);
            imports = distinctImports.stream().toList();

            preprocessOutput();
        } catch (FileNotFoundException fileNotFoundException){
            log.error("File isn't find");
        }
    }

    @Override
    public void preprocessOutput() {
        String metricResult = COUPLING_BETWEEN_OBJECTS + " class " + classDeclaration + " CBO = " +
                imports.size();

        setMetric(metricResult);
    }
}
