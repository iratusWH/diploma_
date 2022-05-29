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
import java.util.List;

/**
 * StaticAnalyzer
 *
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
public class CouplingBetweenObjectsMetricProcessing extends MetricProcessingImpl {

    private List<Name> imports; // лист импортируемых классов в исследуемом файле

    public CouplingBetweenObjectsMetricProcessing() {
        setMetricName(MetricNameEnum.COUPLING_BETWEEN_OBJECTS_METRIC); // ввод названия метрики
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile()); // разбор кода на составляющие для анализа


            imports = compilationUnit.getImports() // получение всех импортируемых классов
                    .stream().filter(imp -> !imp.getNameAsString().contains("java.")) // фильтрация поставляемых с java классов
                    .map(ImportDeclaration::getName) // получение имени импортируемого класса
                    .distinct() // фильтрация повторяющихся классов
                    .toList();

            setMetric(String.valueOf(imports.size())); // вывод метрики
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("File hasn't find");
        }
    }
}
