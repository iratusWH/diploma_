package metrics.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.WhileStmt;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CyclomaticComplexityMetricProcessing extends MetricProcessingImpl {

    private static final String CYCLOMATIC_COMPLEXITY = "Cyclomatic Complexity Metric";

    private List<ClassOrInterfaceDeclaration> classDeclaration;
    private Map<String, Integer> methodNamesWithOperatorsCount;

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());

            List<MethodDeclaration> methodDeclarationList = compilationUnit.findAll(MethodDeclaration.class);
            classDeclaration = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
            methodNamesWithOperatorsCount = new HashMap<>();

            methodDeclarationList.forEach(
                    method -> methodNamesWithOperatorsCount.put(
                            method.getNameAsString(),
                            method.findAll(ForEachStmt.class).size()
                                    + method.findAll(ForStmt.class).size()
                                    + method.findAll(IfStmt.class).size()
                                    + method.findAll(DoStmt.class).size()
                                    + method.findAll(SwitchEntry.class).size()
                                    + method.findAll(WhileStmt.class).size()
                                    + 1
                    )
            );

            preprocessOutput();

        } catch (FileNotFoundException fileNotFoundException) {
            log.info(CYCLOMATIC_COMPLEXITY + " Error");
            setMetric(CYCLOMATIC_COMPLEXITY + " got error");
        }
    }

    @Override
    public void preprocessOutput() {
        String metricResult = CYCLOMATIC_COMPLEXITY + " class " + classDeclaration.get(0).getNameAsString() + " for method " +
                methodNamesWithOperatorsCount.toString()
                        .replace("{", "")
                        .replace("}", "");

        setMetric(metricResult);
    }

}
