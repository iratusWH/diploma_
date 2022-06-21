package support.classes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * Перечисление констант-названий метрик
 */
@RequiredArgsConstructor
public enum MetricNameEnum {
    COUPLING_BETWEEN_OBJECTS_METRIC("Coupling between Objects Metric"),
    CYCLES_COMPLEXITY_METRIC("Cycles Complexity Metric"),
    CYCLOMATIC_COMPLEXITY_METRIC("Cyclomatic Complexity Metric"),
    DEPTH_OF_INHERITANCE_METRIC("Depth of Inheritance Metric"),
    HALSTEAD_METRICS("Halstead Metrics"),
    MAINTAINABILITY_INDEX_METRIC("Maintainability Index Metric"),
    LINES_OF_CODE_METRIC("Lines of code"),
    BRACKET_CHECK("Bracket Check"),
    CLASS_COMPLY_WITH_CONVENTION_CHECK("Class comply with convention check"),
    VARIABLE_DECLARATION_ON_NEW_LINE_CHECK("Variable Declaration on new line check"),
    JAVA_CODE_STYLE_CHECK("Java Code Style Check"),
    GOD_OBJECT_ANTIPATTERN("God Object Antipattern"),
    ANEMIC_DOMAIN_MODEL_ANTIPATTERN("Anemic Domain Model Antipattern"),
    CALL_SUPER_ANTIPATTERN("Call Super Antipattern"),
    OBJECT_CESSPOOL_ANTIPATTERN("Object Cesspool Antipattern"),
    DEFAULT_METRIC_NAME("METRIC");

    final String name;

    String getName() {
        if (Objects.nonNull(this.name)) {
            return this.name;
        }
        return "metric without name";
    }
}
