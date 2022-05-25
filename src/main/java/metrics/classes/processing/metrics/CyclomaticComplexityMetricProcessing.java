package metrics.classes.processing.metrics;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class CyclomaticComplexityMetricProcessing extends MetricProcessingImpl {

    private Map<String, Integer> methodNamesWithOperatorsCount;

    public CyclomaticComplexityMetricProcessing(){
        setMetricName(MetricNameEnum.CYCLOMATIC_COMPLEXITY_METRIC);
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());

            List<MethodDeclaration> methodDeclarationList = compilationUnit.findAll(MethodDeclaration.class);
            List<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
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

        setMetric(
                formatMapToString(methodNamesWithOperatorsCount)
        );

        } catch (FileNotFoundException fileNotFoundException) {
            log.error("Error file not find {}", getMetricName());
        }
    }

    private String formatMapToString(Map<String, Integer> resultMap){
        return resultMap.toString()
                .replace("{", "")
                .replace("}", "");
    }


}
