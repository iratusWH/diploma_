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

/**
 * Класс вычисления метрики Цикломатической сложности
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class CyclomaticComplexityMetricProcessing extends MetricProcessingImpl {

    private Map<String, Integer> methodNamesWithOperatorsCount; // словарь методов и метрики цикломатической сложности

    public CyclomaticComplexityMetricProcessing() {
        setMetricName(MetricNameEnum.CYCLOMATIC_COMPLEXITY_METRIC); // ввод названия метрики
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile()); // разбор кода на составляющие для анализа

            List<MethodDeclaration> methodDeclarationList = compilationUnit.findAll(MethodDeclaration.class); // поиск всех деклараций методов в исследуемом классе
            methodNamesWithOperatorsCount = new HashMap<>(); // инициализация словаря

            methodDeclarationList.forEach( // для каждого метода кладем имя метода и кол-во операторов ветвления
                    method -> methodNamesWithOperatorsCount.put(
                            method.getNameAsString(),
                            (int) (method.stream()
                                    .filter(statement -> (statement instanceof ForEachStmt)
                                            || (statement instanceof ForStmt)
                                            || (statement instanceof IfStmt)
                                            || (statement instanceof DoStmt)
                                            || (statement instanceof SwitchEntry)
                                            || (statement instanceof WhileStmt))
                                    .count() + 1) // подсчет всех операторов ветвления

                    )
            );

            setMetric(
                    formatMapToString(methodNamesWithOperatorsCount) // вывод получившейся метрики
            );

        } catch (FileNotFoundException fileNotFoundException) {
            log.error("Error file not find {}", getMetricName());
        }
    }

    // метод убирающий скобки при приведении словаря к строке
    private String formatMapToString(Map<String, Integer> resultMap) {
        return resultMap.toString()
                .replace("{", "")
                .replace("}", "");
    }


}
