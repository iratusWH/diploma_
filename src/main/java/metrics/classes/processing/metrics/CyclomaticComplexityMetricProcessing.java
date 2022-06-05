package metrics.classes.processing.metrics;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

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
        List<MethodDeclaration> methodDeclarationList = this.getFile().findAll(MethodDeclaration.class); // поиск всех деклараций методов в исследуемом классе
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
                                        || (statement instanceof WhileStmt)
                                        || (statement instanceof CatchClause))
                                .count() + 1) // подсчет всех операторов ветвления

                )
        );

        setMetric(
                formatMapToString(methodNamesWithOperatorsCount) // вывод получившейся метрики
        );

    }

    // метод убирающий скобки при приведении словаря к строке
    private String formatMapToString(Map<String, Integer> resultMap) {
        if (!resultMap.isEmpty()) {
            return resultMap.toString()
                    .replace("{", "")
                    .replace("}", "");
        }
        return "Java class hasn't a method declaration";
    }


}
