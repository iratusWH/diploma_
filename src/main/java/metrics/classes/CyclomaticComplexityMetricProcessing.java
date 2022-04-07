package metrics.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import metrics.classes.implementations.MetricProcessingImpl;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyclomaticComplexityMetricProcessing extends MetricProcessingImpl {

    private final static String CYCLOMATIC_COMPLEXITY = "Cyclomatic Complexity Metric";

    @Override
    public void processMetric(Path path) {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(path.toFile());
            List<MethodDeclaration> methodDeclarationList = compilationUnit.findAll(MethodDeclaration.class);
            List<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
            Map<String, Integer> methodNamesWithOperatorsCount = new HashMap<>();

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

            String metricResult = CYCLOMATIC_COMPLEXITY + " class " + classDeclaration.get(0).getNameAsString() + " for method " +
                    methodNamesWithOperatorsCount.toString()
                            .replace("{", "")
                            .replace("}", "");

            setMetric(metricResult);


        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(CYCLOMATIC_COMPLEXITY + " Error");
            setMetric(CYCLOMATIC_COMPLEXITY + " got error");
        }
    }

}
