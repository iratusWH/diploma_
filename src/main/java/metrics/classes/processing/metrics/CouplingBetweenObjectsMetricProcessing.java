package metrics.classes.processing.metrics;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.List;

/**
 * StaticAnalyzer
 * <p>
 * Класс вычисления метрики связности между классами
 * Допущение:
 * В классе не должны присутствовать подобные импортируемый пакеты:
 * lombok.*
 *
 * @author Маркелов Александр A-07-18
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class CouplingBetweenObjectsMetricProcessing extends SimpleMetricProcessingImpl {

    private List<Name> imports; // лист импортируемых классов в исследуемом файле

    public CouplingBetweenObjectsMetricProcessing() {
        setMetricName(MetricNameEnum.COUPLING_BETWEEN_OBJECTS_METRIC); // ввод названия метрики
    }

    @Override
    public void processMetric() {
        imports = this.getFile().getImports() // получение всех импортируемых классов
                .stream().filter(imp -> !imp.getNameAsString().contains("java.")) // фильтрация поставляемых с java классов
                .map(ImportDeclaration::getName) // получение имени импортируемого класса
                .distinct() // фильтрация повторяющихся классов
                .toList();

        setMetric(String.valueOf(imports.size())); // вывод метрики
    }
}
